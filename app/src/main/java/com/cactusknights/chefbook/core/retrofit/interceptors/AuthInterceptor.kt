package com.cactusknights.chefbook.core.retrofit.interceptors

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.TokensProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.datastore.SessionManager
import com.cactusknights.chefbook.core.retrofit.RefreshToken
import com.cactusknights.chefbook.repositories.remote.api.SessionApi
import kotlinx.coroutines.runBlocking
import okhttp3.*
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val api: SessionApi,
    private val session: SessionManager,
    private val settings: SettingsManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = getAuthRequest(chain.request())
        var response = chain.proceed(request)

        if (response.code == 401) {
            runBlocking {
                val tokenResponse = api.refreshSession(RefreshToken(session.getRefreshToken()))
                if (tokenResponse.isSuccessful) {
                    response.body
                    session.saveTokens(
                        TokensProto.newBuilder()
                            .setAccessToken(tokenResponse.body()?.accessToken)
                            .setRefreshToken(tokenResponse.body()?.refreshToken)
                            .build()
                    )
                }
                response = chain.proceed(getAuthRequest(chain.request()))
                if (response.code == 401) {
                    settings.setUserType(SettingsProto.UserType.OFFLINE)
                    settings.setDataSourceType(SettingsProto.DataSource.LOCAL)
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
            "Bearer ${session.getAccessToken()}"
        ).build()
    }
}