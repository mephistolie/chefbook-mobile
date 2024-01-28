package io.chefbook.sdk.encryption.recipe.impl.data.sources.local

import android.content.Context
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import java.io.File
import java.io.IOException

internal class LocalRecipeEncryptionSourceImpl(
  private val context: Context
) : LocalRecipeEncryptionSource {

  override suspend fun getRecipeKey(recipeId: String): Result<ByteArray> {
    val recipeKeyFile = File(context.filesDir, "$RECIPES_DIR/$recipeId/$RECIPE_KEY_FILE")
    if (!recipeKeyFile.exists()) return Result.failure(NotFoundException())
    return Result.success(recipeKeyFile.readBytes())
  }

  override suspend fun setRecipeKey(recipeId: String, key: ByteArray): EmptyResult {
    return try {
      val recipeDir = File(context.filesDir, "$RECIPES_DIR/$recipeId")
      if (!recipeDir.exists()) recipeDir.mkdirs()
      val recipeKeyFile = File(recipeDir, RECIPE_KEY_FILE)
      recipeKeyFile.writeBytes(key)
      successResult
    } catch (e: IOException) {
      Result.failure(e)
    }
  }

  override suspend fun deleteRecipeKey(recipeId: String) = runCatching {
    val recipeDir = File(context.cacheDir, "$RECIPES_DIR/$recipeId")
    if (!recipeDir.exists()) successResult
    val recipeKeyFile = File(recipeDir, RECIPE_KEY_FILE)
    if (recipeKeyFile.exists()) {
      if (!recipeKeyFile.delete()) Result.failure<Unit>(Exception("unable to modify file"))
    }
  }

  override suspend fun clear() = runCatching {
    val recipeDir = File(context.cacheDir, RECIPES_DIR)
    if (recipeDir.exists()) recipeDir.deleteRecursively()
  }

  companion object {
    const val RECIPES_DIR = "recipes"
    const val RECIPE_KEY_FILE = "recipe_key"
  }
}
