package com.cactusknights.chefbook.data.network.interceptors

import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
    private val tokensRepo: ISessionRepo,
) : Interceptor {

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