package com.cactusknights.chefbook.repositories.remote.datasources

import android.content.SharedPreferences
import com.cactusknights.chefbook.base.Constants.ACCESS_TOKEN
import com.cactusknights.chefbook.base.Constants.REFRESH_TOKEN
import com.cactusknights.chefbook.common.Utils.clearTokens
import com.cactusknights.chefbook.common.Utils.processTokenResponse
import com.cactusknights.chefbook.domain.AuthDataSource
import com.cactusknights.chefbook.domain.SettingsRepository
import com.cactusknights.chefbook.models.DataSource
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.models.UserType
import com.cactusknights.chefbook.models.retrofit.AuthData
import com.cactusknights.chefbook.models.retrofit.RefreshToken
import com.cactusknights.chefbook.repositories.remote.api.SessionApi
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.Interceptor
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class RemoteAuthDataSource @Inject constructor(
    private val api: ChefBookApi,
    private val sp: SharedPreferences
) : AuthDataSource {

    private var user: MutableStateFlow<User?> = MutableStateFlow(null)

    override suspend fun signUp(email: String, password: String) {
        api.signUp(AuthData(email = email, password = password))
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        val response = api.signIn(
            AuthData(
                email = email,
                password = password
            )
        )
        if (response.code() != 200) throw IOException()
        processTokenResponse(sp, response)
        return true
    }

    override suspend fun signOut() {
        try {
            api.signOut(RefreshToken(sp.getString(REFRESH_TOKEN, "").orEmpty()))
        } finally {
            clearTokens(sp)
        }
        user.emit(null)
    }
}

class AuthInterceptor @Inject constructor(
    private val api: SessionApi,
    private val sp: SharedPreferences,
    private val settingsRepository: SettingsRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = getAuthRequest(chain.request())
        var response: okhttp3.Response = chain.proceed(request)

        if (response.code == 401) {
            val refreshToken = sp.getString(REFRESH_TOKEN, "").orEmpty()
            runBlocking {
                val tokenResponse = api.refreshSession(RefreshToken(refreshToken))
                processTokenResponse(sp, tokenResponse)
                response = chain.proceed(getAuthRequest(chain.request()))
                if (response.code == 401) {
                    settingsRepository.setUserType(UserType.LOCAL)
                    settingsRepository.setDataSourceType(DataSource.LOCAL)
                }
            }
        }

        return if (response.code == 429) {
            Thread.sleep(500)
            chain.proceed(getAuthRequest(chain.request()))
        } else {
            response
        }
    }

    private fun getAuthRequest(request: Request): Request {
        return request.newBuilder().addHeader(
            "Authorization",
            "Bearer ${sp.getString(ACCESS_TOKEN, "").orEmpty()}"
        ).build()
    }
}