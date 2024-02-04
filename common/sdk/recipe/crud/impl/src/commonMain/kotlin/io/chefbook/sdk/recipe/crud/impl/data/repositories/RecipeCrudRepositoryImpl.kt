package io.chefbook.sdk.recipe.crud.impl.data.repositories

import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.encryption.recipe.api.internal.data.crypto.RecipeCryptor
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCache
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.local.LocalRecipeCrudSource
import io.chefbook.sdk.recipe.crud.impl.data.models.asDecrypted
import io.chefbook.sdk.recipe.crud.impl.data.models.decryptByInput
import io.chefbook.sdk.recipe.crud.impl.data.models.toNewRecipe
import io.chefbook.sdk.recipe.crud.impl.data.models.toUpdatedRecipe
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.RemoteRecipeCrudSource
import kotlinx.coroutines.flow.Flow

internal class RecipeCrudRepositoryImpl(
  private val localSource: LocalRecipeCrudSource,
  private val remoteSource: RemoteRecipeCrudSource,

  private val cache: RecipesCache,
  private val encryptedVaultRepository: EncryptedVaultRepository,
  private val recipeEncryptionRepository: RecipeEncryptionRepository,
  private val profileRepository: ProfileRepository,
  private val sources: DataSourcesRepository,
  private val cryptor: RecipeCryptor,
) : RecipeCrudRepository {

  override suspend fun observeRecipe(recipeId: String): Flow<Recipe?> {
    when (val cachedRecipe = cache.getRecipe(recipeId)) {
      null -> getRecipe(recipeId)
      is EncryptedRecipe -> cache.putRecipe(decryptRecipe(cachedRecipe))
      else -> Unit
    }
    return cache.observeRecipe(recipeId)
  }

  override suspend fun getRecipe(recipeId: String): Result<Recipe> {
    cache.getRecipe(recipeId)?.let { return Result.success(it) }

    var result = localSource.getRecipe(recipeId)
    if (result.isFailure && sources.isRemoteSourceAvailable()) {
      result = remoteSource.getRecipe(recipeId)
    }

    return result.map { recipe ->
      val processedRecipe = if (recipe is EncryptedRecipe) decryptRecipe(recipe) else recipe
      cache.putRecipe(processedRecipe)
      processedRecipe
    }
  }

  private suspend fun decryptRecipe(recipe: EncryptedRecipe): Recipe =
    encryptedVaultRepository.getVaultPrivateKey()
      .map { vaultKey ->
        recipeEncryptionRepository.getRecipeKey(recipe.id, vaultKey)
          .map { recipeKey -> cryptor.decryptRecipe(recipe, recipeKey) }.getOrNull()
      }
      .getOrNull() ?: recipe

  override suspend fun createRecipe(
    input: RecipeInput,
    key: SymmetricKey?
  ): Result<String> {
    val processedInput =
      input.asDecrypted().let { if (key != null) cryptor.encryptRecipeInput(it, key) else it }

    var recipe = processedInput.toNewRecipe(owner = profileRepository.getProfile().getOrNull())

    if (sources.isRemoteSourceEnabled()) {
      remoteSource.createRecipe(processedInput)
        .onSuccess { id -> recipe = recipe.withId(id = id) }
        .onFailure { e -> return Result.failure(e) }
    }

    localSource.createRecipe(recipe)
      .onFailure { e -> if (!sources.isRemoteSourceEnabled()) return Result.failure(e) }

    (recipe as? EncryptedRecipe)?.let { recipe = it.decryptByInput(input) }
    cache.putRecipe(recipe)

    return Result.success(recipe.id)
  }

  override suspend fun updateRecipe(
    input: RecipeInput,
    key: SymmetricKey?
  ): Result<Recipe> {
    val processedInput =
      input.asDecrypted().let { if (key != null) cryptor.encryptRecipeInput(it, key) else it }

    var updatedRecipe = processedInput.toUpdatedRecipe(getRecipe(input.id).getOrNull())

    if (sources.isRemoteSourceEnabled()) {
      remoteSource.updateRecipe(processedInput)
        .onSuccess { version -> updatedRecipe = updatedRecipe.withVersion(version = version) }
        .onFailure { e -> return Result.failure(e) }
    }

    localSource.updateRecipe(updatedRecipe)
      .onFailure { e -> if (!sources.isRemoteSourceEnabled()) return Result.failure(e) }

    (updatedRecipe as? EncryptedRecipe)?.let { updatedRecipe = it.decryptByInput(input) }
    cache.putRecipe(updatedRecipe)

    return Result.success(updatedRecipe)
  }

  override suspend fun deleteRecipe(recipeId: String): EmptyResult {
    if (sources.isRemoteSourceEnabled()) remoteSource.deleteRecipe(recipeId).onFailure { return Result.failure(it) }

    localSource.deleteRecipe(recipeId)
    cache.removeRecipe(recipeId)

    return successResult
  }
}
