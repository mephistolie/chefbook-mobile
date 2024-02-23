package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.ProfileInfoSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.GetSharedShoppingListLinkResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.url

internal class ShoppingListUsersApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), ShoppingListUsersApiService {

  override suspend fun getShoppingListJoinLink(
    shoppingListId: String,
  ): Result<GetSharedShoppingListLinkResponse> =
    safeGet("$SHOPPING_LISTS_ROUTE/$shoppingListId/link")

  override suspend fun getShoppingListUsers(
    shoppingListId: String,
  ): Result<List<ProfileInfoSerializable>> =
    safeGet("$SHOPPING_LISTS_ROUTE/$shoppingListId/users")

  override suspend fun joinShoppingList(
    shoppingListId: String,
    key: String
  ): Result<MessageResponse> =
    safePost("$SHOPPING_LISTS_ROUTE/$shoppingListId/users")

  // TODO
  override suspend fun deleteShoppingListUser(
    shoppingListId: String,
    userId: String
  ): Result<MessageResponse> =
    safeDelete("$SHOPPING_LISTS_ROUTE/$shoppingListId/users/$userId")

  companion object {
    private const val SHOPPING_LISTS_ROUTE = "/v1/shopping-lists"
  }
}
