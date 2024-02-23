package io.chefbook.sdk.shoppinglist.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.withListCast
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListMetaSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.toSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.ShoppingListApiService
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.SetShoppingListRequest
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.ShoppingListSerializable

internal class RemoteShoppingListDataSourceImpl(
  private val api: ShoppingListApiService,
) : RemoteShoppingListDataSource {

  override suspend fun getShoppingLists() =
    api.getShoppingLists().withListCast(ShoppingListMetaSerializable::toEntity)

  override suspend fun getShoppingList(shoppingListId: String): Result<ShoppingList> =
    api.getShoppingList(shoppingListId).map(ShoppingListSerializable::toEntity)

  override suspend fun getPersonalShoppingList() =
    api.getPersonalShoppingList().map(ShoppingListSerializable::toEntity)

  override suspend fun setShoppingList(shoppingList: ShoppingList) =
    api.setShoppingList(
      shoppingListId = shoppingList.meta.id,
      body = SetShoppingListRequest(
        purchases = shoppingList.purchases.map(Purchase::toSerializable),
        lastVersion = shoppingList.version,
      ),
    ).map(VersionResponse::version)

  override suspend fun addToShoppingList(
    shoppingListId: String,
    purchases: List<Purchase>
  ): Result<Int> =
    api.addToShoppingList(
      shoppingListId = shoppingListId,
      body = SetShoppingListRequest(purchases = purchases.map(Purchase::toSerializable)),
    ).map(VersionResponse::version)

  override suspend fun deleteShoppingList(shoppingListId: String) =
    api.deleteShoppingList(shoppingListId).asEmpty()
}
