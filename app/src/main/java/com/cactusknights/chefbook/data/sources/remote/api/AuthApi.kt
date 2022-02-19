package com.cactusknights.chefbook.data.sources.remote.api

import com.cactusknights.chefbook.core.retrofit.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body authData: AuthData): Response<IdResponse>

    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body authData: AuthData): Response<Tokens>

    @POST("/v1/auth/sign-out")
    suspend fun signOut(@Body elementModel: RefreshToken): Response<MessageResponse>

}