package io.chefbook.sdk.encryption.recipe.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.database.api.internal.RecipeKeys
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64

internal class LocalRecipeEncryptionSourceImpl(
  private val profileId: String,
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalRecipeEncryptionSource {

  private val queries = database.recipeKeyQueries

  override suspend fun getRecipeKey(recipeId: String): Result<ByteArray> = safeQueryResult {
    queries.select(
      profileId = profileId,
      recipeId = recipeId,
    ).executeAsOne().decodeBase64Bytes()
  }

  override suspend fun setRecipeKey(
    recipeId: String,
    encryptedKey: ByteArray,
  ): EmptyResult  = safeQueryResult {
    return@safeQueryResult queries.transaction {
      queries.delete(
        profileId = profileId,
        recipeId = recipeId,
      )
      queries.insert(
        recipeKeys = RecipeKeys(
          profileId = profileId,
          recipeId = recipeId,
          encryptedKey = encryptedKey.encodeBase64(),
        )
      )
    }
  }

  override suspend fun deleteRecipeKey(recipeId: String): EmptyResult = safeQueryResult {
    queries.delete(
      profileId = profileId,
      recipeId = recipeId,
    )
  }

  override suspend fun clear(): EmptyResult = safeQueryResult {
    queries.deleteAll(
      profileId = profileId,
    )
  }
}
