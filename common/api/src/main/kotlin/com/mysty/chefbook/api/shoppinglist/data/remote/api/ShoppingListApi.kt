package com.mysty.chefbook.api.shoppinglist.data.remote.api

import com.mysty.chefbook.api.common.network.dto.RequestResult
import com.mysty.chefbook.api.common.network.dto.responses.MessageResponse
import com.mysty.chefbook.api.shoppinglist.data.common.dto.PurchaseSerializable
import com.mysty.chefbook.api.shoppinglist.data.common.dto.ShoppingListSerializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

internal interface ShoppingListApi {
    @GET("/v1/shopping-list")
    suspend fun getShoppingList(): RequestResult<ShoppingListSerializable>

    @POST("/v1/shopping-list")
    suspend fun setShoppingList(@Body shoppingList: List<PurchaseSerializable>): RequestResult<MessageResponse>

    @PUT("/v1/shopping-list")
    suspend fun addToShoppingList(@Body shoppingList: List<PurchaseSerializable>): RequestResult<MessageResponse>
}
