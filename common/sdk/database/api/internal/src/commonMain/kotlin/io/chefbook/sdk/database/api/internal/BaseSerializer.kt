package io.chefbook.sdk.database.api.internal

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

abstract class BaseSerializer<T>(
  override val defaultValue: T,
) : OkioSerializer<T>

inline fun <reified T> readFromJson(source: BufferedSource, defaultValue: T): T {
  return try {
    Json.decodeFromString<T>(source.readByteArray().decodeToString())
  } catch (e: SerializationException) {
    defaultValue
  }
}

inline fun <reified T> writeToJson(t: T, sink: BufferedSink) {
  sink.write(Json.encodeToString(t).encodeToByteArray())
}
