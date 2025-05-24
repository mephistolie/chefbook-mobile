package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory

internal interface PendingUploadsDataStore : DataStore<Set<String>>

internal class PendingUploadsDataStoreImpl(
  factory: ChefBookDataStoreFactory,
) : PendingUploadsDataStore, DataStore<Set<String>> by   factory.create(
  fileName = "shopping_lists_pending_uploads.json",
  serializer = PendingUploadsSerializer,
)
