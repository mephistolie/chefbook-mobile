package com.cactusknights.chefbook.repositories.remote.api

import com.cactusknights.chefbook.core.retrofit.IdResponse
import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.repositories.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface CategoriesApi {

    @GET("/v1/categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @POST("/v1/categories")
    suspend fun addCategory(@Body category: CategoryDto): Response<IdResponse>

    @PUT("/v1/categories/{category_id}")
    suspend fun updateCategory(@Path("category_id") categoryId: String, @Body category: CategoryDto): Response<MessageResponse>

    @DELETE("/v1/categories/{category_id}")
    suspend fun deleteCategory(@Path("category_id") categoryId: String): Response<MessageResponse>

}