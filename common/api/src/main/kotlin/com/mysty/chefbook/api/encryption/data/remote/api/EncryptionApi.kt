package com.mysty.chefbook.api.encryption.data.remote.api

import com.mysty.chefbook.api.common.network.dto.RequestResult
import com.mysty.chefbook.api.common.network.dto.responses.LinkResponse
import com.mysty.chefbook.api.common.network.dto.responses.MessageResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

internal interface EncryptionApi {

    @GET("/v1/profile/key")
    suspend fun getUserKeyLink() : RequestResult<LinkResponse>

    @Multipart
    @POST("/v1/profile/key")
    suspend fun uploadUserKey(@Part key: MultipartBody.Part): RequestResult<LinkResponse>

    @DELETE("/v1/profile/key")
    suspend fun deleteUserKey(): RequestResult<MessageResponse>

    @GET("/v1/recipes/{recipe_id}/key")
    suspend fun getRecipeKeyLink(@Path("recipe_id") recipeId: String) : RequestResult<LinkResponse>

    @Multipart
    @POST("/v1/recipes/{recipe_id}/key")
    suspend fun uploadRecipeKey(@Path("recipe_id") recipeId: String, @Part key: MultipartBody.Part): RequestResult<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/key")
    suspend fun deleteRecipeKey(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

}
