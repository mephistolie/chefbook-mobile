package io.chefbook.sdk.database.impl

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import io.chefbook.libs.io.IOProvider
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory

class ChefBookDataStoreFactoryImpl(
  private val io: IOProvider,
) : ChefBookDataStoreFactory {

  override fun <T> create(fileName: String, serializer: OkioSerializer<T>): DataStore<T> {
    return DataStoreFactory.create(
      storage = OkioStorage(
        fileSystem = io.fileSystem,
        serializer = serializer,
        producePath = { io.filesDir.resolve(fileName) },
      ),
    )
  }
}
