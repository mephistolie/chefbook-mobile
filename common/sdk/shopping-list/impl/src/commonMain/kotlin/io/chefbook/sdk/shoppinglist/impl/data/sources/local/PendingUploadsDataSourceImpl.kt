package io.chefbook.sdk.shoppinglist.impl.data.sources.local

import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.PendingUploadsDataStore
import kotlinx.coroutines.flow.first

internal class PendingUploadsDataSourceImpl(
  private val dataStore: PendingUploadsDataStore,
) : PendingUploadsDataSource {

  override suspend fun getPendingUploads(): Set<String> =
    dataStore.data.first()

  override suspend fun setPendingUploads(uploads: Set<String>) {
    dataStore.updateData { uploads }
  }

  override suspend fun markPendingUpload(shoppingListId: String) {
    dataStore.updateData { it.plus(shoppingListId) }
  }

  override suspend fun markUploaded(shoppingListId: String) {
    dataStore.updateData { it.minus(shoppingListId) }
  }
}
