package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListMetaSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.ShoppingListSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.CreateSharedShoppingListRequest
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.CreateSharedShoppingListResponse
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.SetShoppingListRequest

internal interface ShoppingListApiService {

  suspend fun getShoppingLists(): Result<List<ShoppingListMetaSerializable>>

  suspend fun createSharedShoppingList(body: CreateSharedShoppingListRequest): Result<CreateSharedShoppingListResponse>

  suspend fun getPersonalShoppingList(): Result<ShoppingListSerializable>

  suspend fun getShoppingList(shoppingListId: String): Result<ShoppingListSerializable>

  suspend fun setShoppingListName(shoppingListId: String, name: String? = null): Result<MessageResponse>

  suspend fun setShoppingList(
    shoppingListId: String,
    body: SetShoppingListRequest,
  ): Result<VersionResponse>

  suspend fun addToShoppingList(
    shoppingListId: String,
    body: SetShoppingListRequest,
  ): Result<VersionResponse>

  suspend fun deleteShoppingList(shoppingListId: String): Result<MessageResponse>
}
