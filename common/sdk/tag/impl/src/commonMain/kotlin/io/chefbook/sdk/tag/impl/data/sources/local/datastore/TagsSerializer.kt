package io.chefbook.sdk.tag.impl.data.sources.local.datastore

import io.chefbook.sdk.database.api.internal.BaseSerializer
import io.chefbook.sdk.database.api.internal.readFromJson
import io.chefbook.sdk.database.api.internal.writeToJson
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable
import okio.BufferedSink
import okio.BufferedSource

internal object TagsSerializer : BaseSerializer<TagsSerializable>(TagsSerializable()) {

  override suspend fun readFrom(source: BufferedSource) = readFromJson(source, defaultValue)

  override suspend fun writeTo(t: TagsSerializable, sink: BufferedSink) = writeToJson(t, sink)
}
