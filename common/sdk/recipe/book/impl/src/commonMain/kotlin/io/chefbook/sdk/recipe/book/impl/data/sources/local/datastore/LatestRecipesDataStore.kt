package io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore.dto.LatestRecipeInfoSerializable

internal interface LatestRecipesDataStore : DataStore<List<LatestRecipeInfoSerializable>>

internal class LatestRecipesDataStoreImpl(
  factory: ChefBookDataStoreFactory,
) : LatestRecipesDataStore, DataStore<List<LatestRecipeInfoSerializable>> by   factory.create(
  fileName = "latest_recipes.json",
  serializer = LatestRecipesSerializer,
)
