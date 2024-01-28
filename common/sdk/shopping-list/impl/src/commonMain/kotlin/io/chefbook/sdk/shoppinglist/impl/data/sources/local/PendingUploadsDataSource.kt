package io.chefbook.sdk.shoppinglist.impl.data.sources.local

internal interface PendingUploadsDataSource {

  suspend fun getPendingUploads(): Set<String>

  suspend fun setPendingUploads(uploads: Set<String>)

  suspend fun markPendingUpload(shoppingListId: String)

  suspend fun markUploaded(shoppingListId: String)
}
