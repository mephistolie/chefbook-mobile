package io.chefbook.sdk.auth.impl.data.sources.local.datastore

import androidx.datastore.core.Serializer
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.SessionSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object SessionSerializer : Serializer<SessionSerializable> {

  override val defaultValue = SessionSerializable()

  override suspend fun readFrom(input: InputStream): SessionSerializable {
    return try {
      Json.decodeFromString<SessionSerializable>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: SessionSerializable, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
