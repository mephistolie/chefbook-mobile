package com.cactusknights.chefbook.repositories.remote.api

import com.cactusknights.chefbook.core.retrofit.LinkResponse
import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.repositories.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface EncryptionApi {

    @GET("/v1/users/key")
    suspend fun getUserKeyLink() : Response<LinkResponse>

    @Multipart
    @POST("/v1/users/key")
    suspend fun uploadUserKey(@Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/users/key")
    suspend fun deleteUserKey(): Response<MessageResponse>

    @Multipart
    @POST("/v1/recipes/{recipe_id}/pictures")
    suspend fun uploadRecipePicture(@Path("recipe_id") recipeId: String, @Part picture: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/pictures")
    suspend fun deleteRecipePicture(@Path("recipe_id") recipeId: String, @Body picture: DeleteRecipePictureInput): Response<MessageResponse>

    @GET("/v1/recipes/{recipe_id}/encryption")
    suspend fun getRecipeKeyLink(@Path("recipe_id") recipeId: String) : Response<LinkResponse>

    @Multipart
    @POST("/v1/recipes/{recipe_id}/encryption")
    suspend fun uploadRecipeKey(@Path("recipe_id") recipeId: String, @Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/encryption")
    suspend fun deleteRecipeKey(@Path("recipe_id") recipeId: String): Response<MessageResponse>
}