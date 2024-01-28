package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore

import androidx.datastore.core.Serializer
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto.ShoppingListSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object ShoppingListsSerializer : Serializer<List<ShoppingListSerializable>> {

  override val defaultValue = emptyList<ShoppingListSerializable>()

  override suspend fun readFrom(input: InputStream): List<ShoppingListSerializable> {
    return try {
      Json.decodeFromString<List<ShoppingListSerializable>>(input.readBytes().decodeToString())
    } catch (e: SerializationException) {
      defaultValue
    }
  }

  override suspend fun writeTo(t: List<ShoppingListSerializable>, output: OutputStream) =
    output.write(Json.encodeToString(t).toByteArray())
}
