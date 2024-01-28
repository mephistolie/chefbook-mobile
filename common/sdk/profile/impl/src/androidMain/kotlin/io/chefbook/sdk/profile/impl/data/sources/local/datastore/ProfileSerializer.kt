package io.chefbook.sdk.profile.impl.data.sources.local.datastore

import androidx.datastore.core.Serializer
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.dto.ProfileSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object ProfileSerializer : Serializer<ProfileSerializable> {

  override val defaultValue: ProfileSerializable = ProfileSerializable()

  override suspend fun readFrom(input: InputStream): ProfileSerializable {
    return try {
      Json.decodeFromString<ProfileSerializable>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: ProfileSerializable, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
