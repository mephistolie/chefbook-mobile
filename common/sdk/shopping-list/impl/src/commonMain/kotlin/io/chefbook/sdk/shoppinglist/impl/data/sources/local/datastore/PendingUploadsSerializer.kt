package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore

import io.chefbook.sdk.database.api.internal.BaseSerializer
import io.chefbook.sdk.database.api.internal.readFromJson
import io.chefbook.sdk.database.api.internal.writeToJson
import okio.BufferedSink
import okio.BufferedSource

internal object PendingUploadsSerializer : BaseSerializer<Set<String>>(emptySet()) {

  override suspend fun readFrom(source: BufferedSource) = readFromJson(source, defaultValue)

  override suspend fun writeTo(t: Set<String>, sink: BufferedSink) = writeToJson(t, sink)
}
