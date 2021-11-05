package com.cactusknights.chefbook.source.remote

import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.retrofit.SignUpResponse
import com.cactusknights.chefbook.models.retrofit.AuthData
import com.cactusknights.chefbook.models.retrofit.RefreshToken
import com.cactusknights.chefbook.models.retrofit.TokenResponse
import com.cactusknights.chefbook.source.remote.dto.RecipeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChefBookApi {

    @POST("/v1/users/sign-up")
    suspend fun signUp(@Body elementModel: AuthData): Response<SignUpResponse>

    @POST("/v1/users/sign-in")
    suspend fun signIn(@Body elementModel: AuthData): Response<TokenResponse>

    @POST("/v1/users/sign-out")
    suspend fun signOut(@Body elementModel: AuthData)

    @POST("/v1/users/refresh")
    suspend fun refreshSession(@Body elementModel: RefreshToken): Response<TokenResponse>

    @GET("/v1/recipes/")
    suspend fun getRecipes(): Response<List<RecipeDto>>
}