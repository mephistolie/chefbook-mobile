package com.cactusknights.chefbook.data.network.api

import com.cactusknights.chefbook.data.dto.remote.categories.CategoryInputRequest
import com.cactusknights.chefbook.data.dto.remote.categories.CategoryResponse
import com.cactusknights.chefbook.data.dto.remote.common.IdResponse
import com.cactusknights.chefbook.data.dto.remote.common.MessageResponse
import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApi {

    @GET("/v1/categories")
    suspend fun getCategories(): RequestResult<List<CategoryResponse>>

    @POST("/v1/categories")
    suspend fun addCategory(@Body category: CategoryInputRequest): RequestResult<IdResponse>

    @GET("/v1/categories/{category_id}")
    suspend fun getCategory(@Path("category_id") categoryId: Int): RequestResult<CategoryResponse>

    @PUT("/v1/categories/{category_id}")
    suspend fun updateCategory(@Path("category_id") categoryId: Int, @Body category: CategoryInputRequest): RequestResult<MessageResponse>

    @DELETE("/v1/categories/{category_id}")
    suspend fun deleteCategory(@Path("category_id") categoryId: Int): RequestResult<MessageResponse>

}
