package com.mysty.chefbook.api.auth.data.remote.api

import com.mysty.chefbook.api.auth.data.remote.dto.CredentialsBody
import com.mysty.chefbook.api.auth.data.remote.dto.RefreshTokenRequest
import com.mysty.chefbook.api.auth.data.remote.dto.TokensResponse
import com.mysty.chefbook.api.common.constants.Endpoints.V1
import com.mysty.chefbook.api.common.network.dto.RequestResult
import com.mysty.chefbook.api.common.network.dto.responses.IdResponse
import com.mysty.chefbook.api.common.network.dto.responses.MessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface AuthApi {

    @POST("/$V1/auth/sign-up")
    suspend fun signUp(@Body authData: CredentialsBody): RequestResult<IdResponse>

    @GET("/$V1/auth/activate/{activation_code}")
    suspend fun activateProfile(@Path("activation_code") activationCode: String): RequestResult<MessageResponse>

    @POST("/$V1/auth/sign-in")
    suspend fun signIn(@Body authData: CredentialsBody): RequestResult<TokensResponse>

    @POST("/$V1/auth/refresh")
    suspend fun refreshSession(@Body elementModel: RefreshTokenRequest): RequestResult<TokensResponse>

    @POST("/$V1/auth/sign-out")
    suspend fun signOut(@Body elementModel: RefreshTokenRequest): RequestResult<MessageResponse>

}
