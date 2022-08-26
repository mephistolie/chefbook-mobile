package com.cactusknights.chefbook.data.network.api

import com.cactusknights.chefbook.data.dto.remote.auth.CredentialsBody
import com.cactusknights.chefbook.data.dto.remote.auth.RefreshTokenRequest
import com.cactusknights.chefbook.data.dto.remote.auth.TokensResponse
import com.cactusknights.chefbook.data.dto.remote.common.IdResponse
import com.cactusknights.chefbook.data.dto.remote.common.MessageResponse
import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body authData: CredentialsBody): RequestResult<IdResponse>

    @GET("/v1/auth/activate/{activation_code}")
    suspend fun activateProfile(@Path("activation_code") activationCode: String): RequestResult<MessageResponse>

    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body authData: CredentialsBody): RequestResult<TokensResponse>

    @POST("/v1/auth/refresh")
    suspend fun refreshSession(@Body elementModel: RefreshTokenRequest): RequestResult<TokensResponse>

    @POST("/v1/auth/sign-out")
    suspend fun signOut(@Body elementModel: RefreshTokenRequest): RequestResult<MessageResponse>

}
