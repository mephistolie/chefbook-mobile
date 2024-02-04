package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListMetaSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.CreateSharedShoppingListRequest
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.CreateSharedShoppingListResponse
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.SetShoppingListNameRequest
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.SetShoppingListRequest
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.ShoppingListSerializable
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class ShoppingListApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), ShoppingListApiService {

  override suspend fun getShoppingLists(): Result<List<ShoppingListMetaSerializable>> = safeGet {
    url(SHOPPING_LISTS_ROUTE)
  }

  override suspend fun createSharedShoppingList(
    body: CreateSharedShoppingListRequest,
  ): Result<CreateSharedShoppingListResponse> = safePost {
    url(SHOPPING_LISTS_ROUTE)
    setBody(body)
  }

  override suspend fun getPersonalShoppingList(): Result<ShoppingListSerializable> = safeGet {
    url("$SHOPPING_LISTS_ROUTE/personal")
  }

  override suspend fun getShoppingList(shoppingListId: String): Result<ShoppingListSerializable> =
    safeGet {
      url("$SHOPPING_LISTS_ROUTE/$shoppingListId")
    }

  override suspend fun setShoppingListName(
    shoppingListId: String,
    name: String?,
  ): Result<MessageResponse> = safePut {
    url("$SHOPPING_LISTS_ROUTE/$shoppingListId/name")
    setBody(SetShoppingListNameRequest(name))
  }

  override suspend fun setShoppingList(
    shoppingListId: String,
    body: SetShoppingListRequest,
  ): Result<VersionResponse> = safePut {
    url("$SHOPPING_LISTS_ROUTE/$shoppingListId")
    setBody(body)
  }

  override suspend fun addToShoppingList(
    shoppingListId: String,
    body: SetShoppingListRequest,
  ): Result<VersionResponse> = safePatch {
    url("$SHOPPING_LISTS_ROUTE/$shoppingListId")
    setBody(body)
  }

  override suspend fun deleteShoppingList(shoppingListId: String): Result<MessageResponse> =
    safeDelete {
      url("$SHOPPING_LISTS_ROUTE/$shoppingListId")
    }

  companion object {
    private const val SHOPPING_LISTS_ROUTE = "/v1/shopping-lists"
  }
}
