package io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore

import androidx.datastore.core.Serializer
import io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore.dto.LatestRecipeInfoSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object LatestRecipesSerializer : Serializer<List<LatestRecipeInfoSerializable>> {

  override val defaultValue = emptyList<LatestRecipeInfoSerializable>()

  override suspend fun readFrom(input: InputStream): List<LatestRecipeInfoSerializable> {
    return try {
      Json.decodeFromString<List<LatestRecipeInfoSerializable>>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: List<LatestRecipeInfoSerializable>, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
