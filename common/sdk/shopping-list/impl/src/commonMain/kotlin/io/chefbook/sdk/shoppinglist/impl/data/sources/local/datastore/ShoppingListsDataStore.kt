package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto.ShoppingListSerializable

internal interface ShoppingListsDataStore : DataStore<Map<String, List<ShoppingListSerializable>>>

internal class ShoppingListsDataStoreImpl(
  factory: ChefBookDataStoreFactory,
) : ShoppingListsDataStore, DataStore<Map<String, List<ShoppingListSerializable>>> by factory.create(
  fileName = "shopping_lists.json",
  serializer = ShoppingListsSerializer,
)
