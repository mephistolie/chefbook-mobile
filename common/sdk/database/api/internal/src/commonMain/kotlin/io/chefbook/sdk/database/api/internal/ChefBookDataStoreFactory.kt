package io.chefbook.sdk.database.api.internal

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.OkioSerializer

interface ChefBookDataStoreFactory {

  fun <T> create(fileName: String, serializer: OkioSerializer<T>): DataStore<T>
}
