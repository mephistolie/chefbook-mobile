package com.mysty.chefbook.api.category.data.remote.api

import com.mysty.chefbook.api.category.data.remote.dto.CategoryInputRequest
import com.mysty.chefbook.api.category.data.remote.dto.CategoryResponse
import com.mysty.chefbook.api.common.network.dto.RequestResult
import com.mysty.chefbook.api.common.network.dto.responses.IdResponse
import com.mysty.chefbook.api.common.network.dto.responses.MessageResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface CategoryApi {

    @GET("/v1/categories")
    suspend fun getCategories(): RequestResult<List<CategoryResponse>>

    @POST("/v1/categories")
    suspend fun addCategory(@Body category: CategoryInputRequest): RequestResult<IdResponse>

    @GET("/v1/categories/{category_id}")
    suspend fun getCategory(@Path("category_id") categoryId: String): RequestResult<CategoryResponse>

    @PUT("/v1/categories/{category_id}")
    suspend fun updateCategory(@Path("category_id") categoryId: String, @Body category: CategoryInputRequest): RequestResult<MessageResponse>

    @DELETE("/v1/categories/{category_id}")
    suspend fun deleteCategory(@Path("category_id") categoryId: String): RequestResult<MessageResponse>

}
