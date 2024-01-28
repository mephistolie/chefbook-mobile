package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.ProfileInfoSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.GetSharedShoppingListLinkResponse

internal interface ShoppingListUsersApiService {

  suspend fun getShoppingListJoinLink(shoppingListId: String): Result<GetSharedShoppingListLinkResponse>

  suspend fun getShoppingListUsers(shoppingListId: String): Result<List<ProfileInfoSerializable>>

  suspend fun joinShoppingList(shoppingListId: String, key: String): Result<MessageResponse>

  suspend fun deleteShoppingListUser(shoppingListId: String, userId: String): Result<MessageResponse>
}
