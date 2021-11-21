package com.cactusknights.chefbook.repositories.remote.datasources

import android.content.SharedPreferences
import com.cactusknights.chefbook.common.Constants.ACCESS_TOKEN
import com.cactusknights.chefbook.common.Constants.REFRESH_TOKEN
import com.cactusknights.chefbook.domain.AuthDataSource
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.models.retrofit.AuthData
import com.cactusknights.chefbook.models.retrofit.RefreshToken
import com.cactusknights.chefbook.models.retrofit.TokenResponse
import com.cactusknights.chefbook.repositories.remote.api.SessionApi
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.toUser
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteAuthDataSource @Inject constructor(
    private val api: ChefBookApi,
    private val authInterceptor: AuthInterceptor
) : AuthDataSource {

    private var user: MutableStateFlow<User?> = MutableStateFlow(null)

    override suspend fun signUp(email: String, password: String) {
        api.signUp(AuthData(email = email, password = password))
    }

    override suspend fun signIn(email: String, password: String) {
        val response = api.signIn(
            AuthData(
                email = email,
                password = password
            )
        )
        authInterceptor.processTokenResponse(response)
    }

    suspend fun getUserInfo(): User {
        val response = api.getUserInfo()
        return response.body()!!.toUser()
    }

    override suspend fun signOut() {
        authInterceptor.signOut()
        user.emit(null)
    }
}

class AuthInterceptor @Inject constructor(
    private val api: SessionApi,
    val preferences: SharedPreferences
) : Interceptor {

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = getAuthRequest(chain.request())
        var response: okhttp3.Response = chain.proceed(request)

        if (response.code == 401) {
            val refreshToken = preferences.getString(REFRESH_TOKEN, "").orEmpty()
            runBlocking {
                val tokenResponse = api.refreshSession(RefreshToken(refreshToken))
                processTokenResponse(tokenResponse)
                response = chain.proceed(getAuthRequest(chain.request()))
            }
        }

        return if (response.code == 429) {
            Thread.sleep(1000)
            chain.proceed(getAuthRequest(chain.request()))
        } else {
            response
        }
    }

    private fun getAuthRequest(request: Request): Request {
        return request.newBuilder().addHeader(
            "Authorization",
            "Bearer ${preferences.getString(ACCESS_TOKEN, "").orEmpty()}"
        ).build()
    }

    fun processTokenResponse(response: Response<TokenResponse>) {
        val tokens = response.body()
        if (response.isSuccessful && tokens != null) {
            preferences.edit()
                .putString(ACCESS_TOKEN, tokens.accessToken)
                .putString(REFRESH_TOKEN, tokens.refreshToken)
                .apply()
        } else {
            throw IOException()
        }
    }

    fun signOut() {
        runBlocking {
            try {
                api.signOut(RefreshToken(preferences.getString(REFRESH_TOKEN, "").orEmpty()))
            } finally {
                clearTokens()
            }
        }
    }

    private fun clearTokens() {
        preferences.edit()
            .putString(ACCESS_TOKEN, null)
            .putString(REFRESH_TOKEN, null)
            .apply()
    }
}