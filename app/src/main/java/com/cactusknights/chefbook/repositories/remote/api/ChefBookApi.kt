package com.cactusknights.chefbook.repositories.remote.api

import com.cactusknights.chefbook.models.retrofit.SignUpResponse
import com.cactusknights.chefbook.models.retrofit.AuthData
import com.cactusknights.chefbook.models.retrofit.RefreshToken
import com.cactusknights.chefbook.models.retrofit.TokenResponse
import com.cactusknights.chefbook.repositories.remote.dto.RecipeDto
import com.cactusknights.chefbook.repositories.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChefBookApi {

    @POST("/v1/users/sign-up")
    suspend fun signUp(@Body elementModel: AuthData): Response<SignUpResponse>

    @POST("/v1/users/sign-in")
    suspend fun signIn(@Body elementModel: AuthData): Response<TokenResponse>

    @GET("/v1/users")
    suspend fun getUserInfo(): Response<UserDto>

    @POST("/v1/users/sign-out")
    suspend fun signOut(@Body elementModel: AuthData)

    @POST("/v1/users/refresh")
    suspend fun refreshSession(@Body elementModel: RefreshToken): Response<TokenResponse>

    @GET("/v1/recipes")
    suspend fun getRecipes(): Response<List<RecipeDto>>
}