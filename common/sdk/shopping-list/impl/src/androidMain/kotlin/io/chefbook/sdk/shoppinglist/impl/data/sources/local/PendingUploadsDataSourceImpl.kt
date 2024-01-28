package io.chefbook.sdk.shoppinglist.impl.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.PendingUploadsSerializer
import kotlinx.coroutines.flow.first

internal class PendingUploadsDataSourceImpl(
  context: Context,
) : PendingUploadsDataSource {

  private val dataStore = DataStoreFactory.create(
    serializer = PendingUploadsSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

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

  companion object {
    private const val DATASTORE_FILE = "shopping_lists_pending_uploads.json"
  }
}
