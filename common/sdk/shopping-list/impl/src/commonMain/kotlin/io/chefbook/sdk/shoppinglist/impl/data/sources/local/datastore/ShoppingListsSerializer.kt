package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore

import io.chefbook.sdk.database.api.internal.BaseSerializer
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto.ShoppingListSerializable
import okio.BufferedSink
import okio.BufferedSource

internal object ShoppingListsSerializer
  : BaseSerializer<Map<String, List<ShoppingListSerializable>>>(emptyMap()) {

  override suspend fun readFrom(source: BufferedSource) = readFromJson(source, defaultValue)

  override suspend fun writeTo(t: Map<String, List<ShoppingListSerializable>>, sink: BufferedSink) =
    writeToJson(t, sink)
}
