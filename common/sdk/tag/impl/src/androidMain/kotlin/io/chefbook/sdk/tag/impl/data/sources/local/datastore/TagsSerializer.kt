package io.chefbook.sdk.tag.impl.data.sources.local.datastore

import androidx.datastore.core.Serializer
import io.chefbook.sdk.tag.impl.data.sources.common.dto.TagsSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object TagsSerializer : Serializer<TagsSerializable> {

  override val defaultValue: TagsSerializable = TagsSerializable()

  override suspend fun readFrom(input: InputStream): TagsSerializable {
    return try {
      Json.decodeFromString<TagsSerializable>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: TagsSerializable, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
