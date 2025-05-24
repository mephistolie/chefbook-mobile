package io.chefbook.sdk.settings.impl.data.sources.local.datastore

import io.chefbook.sdk.database.api.internal.BaseSerializer
import io.chefbook.sdk.settings.impl.data.sources.local.datastore.dto.SettingsSerializable
import okio.BufferedSink
import okio.BufferedSource

internal object SettingsSerializer : BaseSerializer<SettingsSerializable>(SettingsSerializable()) {

  override suspend fun readFrom(source: BufferedSource) = readFromJson(source, defaultValue)

  override suspend fun writeTo(t: SettingsSerializable, sink: BufferedSink) = writeToJson(t, sink)
}
