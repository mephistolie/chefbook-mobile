package io.chefbook.sdk.profile.impl.data.sources.local.datastore

import io.chefbook.sdk.database.api.internal.BaseSerializer
import io.chefbook.sdk.database.api.internal.readFromJson
import io.chefbook.sdk.database.api.internal.writeToJson
import io.chefbook.sdk.profile.impl.data.sources.common.dto.ProfileSerializable
import okio.BufferedSink
import okio.BufferedSource

internal object ProfilesSerializer :
  BaseSerializer<Map<String, ProfileSerializable>>(emptyMap()) {

  override suspend fun readFrom(source: BufferedSource) =
    readFromJson(source, defaultValue)

  override suspend fun writeTo(
    t: Map<String, ProfileSerializable>,
    sink: BufferedSink
  ) = writeToJson(t, sink)
}
