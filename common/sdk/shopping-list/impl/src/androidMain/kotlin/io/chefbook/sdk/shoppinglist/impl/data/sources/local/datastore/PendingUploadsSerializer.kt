package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object PendingUploadsSerializer : Serializer<Set<String>> {

  override val defaultValue = setOf<String>()

  override suspend fun readFrom(input: InputStream): Set<String> {
    return try {
      Json.decodeFromString<Set<String>>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: Set<String>, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
