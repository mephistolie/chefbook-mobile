package io.chefbook.sdk.shoppinglist.impl.data.sources.remote

import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.withCast
import io.chefbook.libs.utils.result.withListCast
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.impl.data.sources.ShoppingListDataSource
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListMetaSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.ShoppingListSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.toSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.ShoppingListApiService
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto.SetShoppingListRequest

internal class RemoteShoppingListDataSourceImpl(
  private val api: ShoppingListApiService,
) : RemoteShoppingListDataSource {

  override suspend fun getShoppingLists() =
    api.getShoppingLists().withListCast(ShoppingListMetaSerializable::toEntity)

  override suspend fun getShoppingList(shoppingListId: String): Result<ShoppingList> =
    api.getShoppingList(shoppingListId).withCast(ShoppingListSerializable::toEntity)

  override suspend fun getPersonalShoppingList() =
    api.getPersonalShoppingList().withCast(ShoppingListSerializable::toEntity)

  override suspend fun setShoppingList(shoppingList: ShoppingList) =
    api.setShoppingList(
      shoppingListId = shoppingList.meta.id,
      body = SetShoppingListRequest(
        purchases = shoppingList.purchases.map(Purchase::toSerializable),
        lastVersion = shoppingList.version,
      ),
    ).withCast(VersionResponse::version)

  override suspend fun addToShoppingList(
    shoppingListId: String,
    purchases: List<Purchase>
  ): Result<Int> =
    api.addToShoppingList(
      shoppingListId = shoppingListId,
      body = SetShoppingListRequest(purchases = purchases.map(Purchase::toSerializable)),
    ).withCast(VersionResponse::version)

  override suspend fun deleteShoppingList(shoppingListId: String) =
    api.deleteShoppingList(shoppingListId).asEmpty()
}
