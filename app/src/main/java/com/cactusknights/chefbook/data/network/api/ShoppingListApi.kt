package com.cactusknights.chefbook.data.network.api

import com.cactusknights.chefbook.data.dto.common.recipe.PurchaseSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.ShoppingListSerializable
import com.cactusknights.chefbook.data.dto.remote.common.MessageResponse
import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ShoppingListApi {
    @GET("/v1/shopping-list")
    suspend fun getShoppingList(): RequestResult<ShoppingListSerializable>

    @POST("/v1/shopping-list")
    suspend fun setShoppingList(@Body shoppingList: List<PurchaseSerializable>): RequestResult<MessageResponse>

    @PUT("/v1/shopping-list")
    suspend fun addToShoppingList(@Body shoppingList: List<PurchaseSerializable>): RequestResult<MessageResponse>
}
