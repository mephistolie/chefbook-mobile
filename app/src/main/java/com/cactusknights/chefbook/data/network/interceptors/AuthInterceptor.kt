package com.cactusknights.chefbook.data.network.interceptors

import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val tokensRepo: ISessionRepo,
) : Interceptor {

    companion object {
        private const val SERVER_ERROR = 400
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val authorizedRequest = getAuthRequest(chain.request())
        return chain.proceed(authorizedRequest)
    }

    private fun getAuthRequest(request: Request): Request =
        request.newBuilder().header(
            "Authorization",
            "Bearer ${tokensRepo.getAccessToken()}"
        ).build()

}