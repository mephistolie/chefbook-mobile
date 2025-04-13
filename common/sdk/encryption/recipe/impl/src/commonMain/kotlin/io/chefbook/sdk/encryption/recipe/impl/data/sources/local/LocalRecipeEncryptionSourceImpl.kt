package io.chefbook.sdk.encryption.recipe.impl.data.sources.local

import io.chefbook.libs.io.IOProvider
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import okio.BufferedSource
import okio.Path

internal class LocalRecipeEncryptionSourceImpl(
  profileId: String,
  io: IOProvider,
) : LocalRecipeEncryptionSource {

  private val files = io.fileSystem
  private val encryptionDir = io.filesDir.resolve("encryption/$profileId/recipes")

  override suspend fun getRecipeKey(recipeId: String): Result<ByteArray> {
    val recipeKeyFile = getRecipeKeyPath(recipeId)
    if (!files.exists(recipeKeyFile)) return Result.failure(NotFoundException())
    return Result.success(files.read(recipeKeyFile, BufferedSource::readByteArray))
  }

  override suspend fun setRecipeKey(recipeId: String, key: ByteArray): EmptyResult {
    return runCatching {
      deleteRecipeKey(recipeId)

      if (!files.exists(encryptionDir)) files.createDirectories(encryptionDir)

      val recipeKeyFile = getRecipeKeyPath(recipeId)
      files.write(recipeKeyFile) { write(key) }

      successResult
    }
  }

  override suspend fun deleteRecipeKey(recipeId: String) = runCatching {
    if (!files.exists(encryptionDir)) successResult
    val recipeKeyFile = getRecipeKeyPath(recipeId)
    if (files.exists(recipeKeyFile)) {
      files.deleteRecursively(recipeKeyFile)
    }
  }

  override suspend fun clear() = runCatching {
    if (files.exists(encryptionDir)) files.deleteRecursively(encryptionDir)
  }

  private fun getRecipeKeyPath(recipeId: String): Path {
    return encryptionDir.resolve(recipeId)
  }
}
