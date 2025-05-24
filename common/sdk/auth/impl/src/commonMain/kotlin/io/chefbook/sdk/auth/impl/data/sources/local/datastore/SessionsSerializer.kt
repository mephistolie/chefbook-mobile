package io.chefbook.sdk.auth.impl.data.sources.local.datastore

import io.chefbook.sdk.database.api.internal.BaseSerializer
import io.chefbook.sdk.auth.impl.data.sources.local.dto.SessionsInfo
import okio.BufferedSink
import okio.BufferedSource

internal object SessionsSerializer :
  BaseSerializer<SessionsInfo>(SessionsInfo()) {

  override suspend fun readFrom(source: BufferedSource) =
    readFromJson(source, defaultValue)

  override suspend fun writeTo(t: SessionsInfo, sink: BufferedSink) =
    writeToJson(t, sink)
}
