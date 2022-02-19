package com.cactusknights.chefbook.data.sources.remote.api

import com.cactusknights.chefbook.core.retrofit.RefreshToken
import com.cactusknights.chefbook.core.retrofit.Tokens
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionApi {
    @POST("/v1/auth/refresh")
    suspend fun refreshSession(@Body elementModel: RefreshToken): Response<Tokens>
}