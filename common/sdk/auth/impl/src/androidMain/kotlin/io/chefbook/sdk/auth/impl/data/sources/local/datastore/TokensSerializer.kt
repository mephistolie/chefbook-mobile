package io.chefbook.sdk.auth.impl.data.sources.local.datastore

import androidx.datastore.core.Serializer
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto.TokensSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object TokensSerializer : Serializer<TokensSerializable> {

  override val defaultValue = TokensSerializable()

  override suspend fun readFrom(input: InputStream): TokensSerializable {
    return try {
      Json.decodeFromString<TokensSerializable>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: TokensSerializable, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
