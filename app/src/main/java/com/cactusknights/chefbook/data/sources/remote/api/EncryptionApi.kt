package com.cactusknights.chefbook.data.sources.remote.api

import com.cactusknights.chefbook.core.retrofit.LinkResponse
import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.data.sources.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface EncryptionApi {

    @GET("/v1/profile/key")
    suspend fun getUserKeyLink() : Response<LinkResponse>

    @Multipart
    @POST("/v1/profile/key")
    suspend fun uploadUserKey(@Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/profile/key")
    suspend fun deleteUserKey(): Response<MessageResponse>

    @GET("/v1/recipes/{recipe_id}/encryption")
    suspend fun getRecipeKeyLink(@Path("recipe_id") recipeId: String) : Response<LinkResponse>

    @Multipart
    @POST("/v1/recipes/{recipe_id}/encryption")
    suspend fun uploadRecipeKey(@Path("recipe_id") recipeId: String, @Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/encryption")
    suspend fun deleteRecipeKey(@Path("recipe_id") recipeId: String): Response<MessageResponse>
}