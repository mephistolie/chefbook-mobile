package io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore

import io.chefbook.sdk.database.api.internal.BaseSerializer
import io.chefbook.sdk.database.api.internal.readFromJson
import io.chefbook.sdk.database.api.internal.writeToJson
import io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore.dto.LatestRecipeInfoSerializable
import okio.BufferedSink
import okio.BufferedSource

internal object LatestRecipesSerializer :
  BaseSerializer<List<LatestRecipeInfoSerializable>>(emptyList()) {

  override suspend fun readFrom(source: BufferedSource) = readFromJson(source, defaultValue)

  override suspend fun writeTo(t: List<LatestRecipeInfoSerializable>, sink: BufferedSink) =
    writeToJson(t, sink)
}
