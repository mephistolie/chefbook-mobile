package com.cactusknights.chefbook.data.sources.remote.api

import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.data.sources.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ShoppingListApi {
    @GET("/v1/shopping-list")
    suspend fun getShoppingList(): Response<ShoppingListDto>

    @POST("/v1/shopping-list")
    suspend fun setShoppingList(@Body shoppingList: ShoppingListInputDto): Response<MessageResponse>

    @PUT("/v1/shopping-list")
    suspend fun addToShoppingList(@Body shoppingList: List<PurchaseDto>): Response<MessageResponse>
}