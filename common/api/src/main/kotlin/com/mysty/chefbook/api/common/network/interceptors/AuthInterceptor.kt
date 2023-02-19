package com.mysty.chefbook.api.common.network.interceptors

import com.mysty.chefbook.api.auth.domain.ITokensRepo
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class AuthInterceptor(
    private val tokensRepo: ITokensRepo,
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
