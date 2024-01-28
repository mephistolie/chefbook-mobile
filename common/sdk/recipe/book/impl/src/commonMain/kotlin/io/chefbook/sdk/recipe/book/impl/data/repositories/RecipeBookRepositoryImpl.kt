package io.chefbook.sdk.recipe.book.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.encryption.AsymmetricPrivateKey
import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.encryption.recipe.api.internal.data.crypto.RecipeCryptor
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.recipe.book.api.external.domain.entities.RecipeBook
import io.chefbook.sdk.recipe.book.api.internal.data.cache.RecipeBookCache
import io.chefbook.sdk.recipe.book.api.internal.data.models.RecipeBookState
import io.chefbook.sdk.recipe.book.api.internal.data.models.RecipeState
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.RecipeBookRepository
import io.chefbook.sdk.recipe.book.impl.data.sources.local.LocalRecipeBookSource
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.RemoteRecipeBookSource
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.RecipeCrudSource
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.local.LocalRecipeCrudSource
import io.chefbook.sdk.recipe.interaction.api.internal.data.sources.local.LocalRecipeInteractionSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class RecipeBookRepositoryImpl(
  private val localSource: LocalRecipeBookSource,
  private val remoteSource: RemoteRecipeBookSource,

  private val localCrudSource: LocalRecipeCrudSource,
  private val remoteCrudSource: RecipeCrudSource,
  private val localInteractionSource: LocalRecipeInteractionSource,

  private val sources: DataSourcesRepository,
  private val cache: RecipeBookCache,
  private val encryptedVaultRepository: EncryptedVaultRepository,
  private val recipeEncryptionRepository: RecipeEncryptionRepository,
  private val categoriesRepository: CategoryRepository,
  private val cryptor: RecipeCryptor,
  private val dispatchers: AppDispatchers,
  private val scopes: CoroutineScopes,
) : RecipeBookRepository {

  private var syncDataJob: Job? = null
  private var updateEncryptionStateJob: Job? = null

  init {
    launchUpdateEncryptionStateJob()
  }

  override fun observeRecipeBook(): Flow<RecipeBook?> {
    if (syncDataJob == null) launchSyncDataJob()
    return cache.observeRecipeBook()
  }

  override suspend fun getRecipeBook(): RecipeBook {
    return cache.getRecipeBook()
  }

  override suspend fun refreshRecipeBook(): EmptyResult {
    if (updateEncryptionStateJob == null) launchUpdateEncryptionStateJob()
    launchSyncDataJob()

    return successResult
  }

  private fun launchSyncDataJob() {
    syncDataJob?.cancel()
    syncDataJob = scopes.repository.launch {
      while (isActive) {
        syncData()
        delay(REFRESH_TIME_THRESHOLD)
      }
    }
  }

  private suspend fun syncData() {
    val localResult = localSource.getRecipeBook()
    localResult.onSuccess(cache::setRecipeBook)

    if (!sources.isRemoteSourceEnabled()) return

    remoteSource.getRecipeBook().onSuccess { recipeBookState ->
      categoriesRepository.cacheCategories(recipeBookState.categories)
      encryptedVaultRepository.refreshEncryptedVaultState(recipeBookState.isEncryptedVaultEnabled)

      val remoteRecipes = cache.getRecipeBook().recipes.map { localRecipe ->
        val recipeState = recipeBookState.recipes.find { it.id == localRecipe.id }
        if (recipeState != null) localRecipe.withState(recipeState) else localRecipe
      }

      cache.setRecipeBook(remoteRecipes)
      pullChanges(
        local = if (localResult.isSuccess) localResult.getOrThrow() else emptyList(),
        remote = recipeBookState,
      )
    }
  }

  private suspend fun pullChanges(local: List<RecipeInfo>, remote: RecipeBookState) {
    for (remoteRecipe in remote.recipes) {
      val localRecipe = local.find { it.id == remoteRecipe.id }
      if (localRecipe == null || remoteRecipe.version > localRecipe.version) {
        remoteCrudSource.getRecipe(remoteRecipe.id).onSuccess { recipe ->
          if (localRecipe == null) {
            localCrudSource.createRecipe(recipe)
          } else {
            localCrudSource.updateRecipe(recipe)
          }
          cache.putRecipe(recipe)
        }
      } else {
        pullRecipeInteractions(localRecipe, remoteRecipe)
      }
    }

    val remoteIds = remote.recipes.map { it.id }
    val deletedRecipes = local.filter { it.id !in remoteIds }
    for (recipe in deletedRecipes) {
      localCrudSource.deleteRecipe(recipe.id)
    }
  }

  private suspend fun pullRecipeInteractions(local: RecipeInfo?, remote: RecipeState) {
    if (local?.rating != remote.rating) {
      localInteractionSource.setRecipeRating(remote.id, remote.rating)
    }

    if (local?.isFavourite != remote.isFavourite) {
      localInteractionSource.setRecipeFavouriteStatus(remote.id, remote.isFavourite)
    }

    val localCategoriesIds = local?.categories?.map { it.id } ?: emptyList()
    val remoteCategoriesIds = remote.categories.map { it.id }
    if (localCategoriesIds.any { it !in remoteCategoriesIds } || remoteCategoriesIds.any { it !in localCategoriesIds }) {
      localInteractionSource.setRecipeCategories(remote.id, remoteCategoriesIds)
    }
  }

  private suspend fun decryptRecipes(vaultKey: AsymmetricPrivateKey) =
    withContext(dispatchers.computation) {
      val recipeBook = cache.getRecipeBook()
      cache.setRecipeBook(recipeBook.recipes.map { recipe ->
        return@map when (recipe) {
          is DecryptedRecipeInfo -> recipe
          is EncryptedRecipeInfo -> decryptRecipe(recipe, vaultKey)
        }
      })
    }

  private suspend fun decryptRecipe(
    recipe: EncryptedRecipeInfo,
    key: AsymmetricPrivateKey
  ): RecipeInfo {
    val recipeKey = getRecipeKey(recipe.id, key) ?: return recipe
    return cryptor.decryptRecipeInfo(recipe, recipeKey)
  }

  private suspend fun getRecipeKey(recipeId: String, key: AsymmetricPrivateKey) =
    recipeEncryptionRepository.getRecipeKey(recipeId, key).getOrNull()

  private fun launchUpdateEncryptionStateJob() {
    updateEncryptionStateJob?.cancel()
    updateEncryptionStateJob = scopes.repository.launch {
      encryptedVaultRepository.observeEncryptedVaultState()
        .collect { state ->
          when (state) {
            EncryptedVaultState.UNLOCKED -> encryptedVaultRepository.getVaultPrivateKey()
              .onSuccess(::decryptRecipes)

            EncryptedVaultState.LOCKED -> resetDecryptedRecipesCache()
            EncryptedVaultState.DISABLED -> deleteLocalEncryptedRecipes()
            EncryptedVaultState.LOADING -> Unit
          }
        }
    }
  }

  private suspend fun resetDecryptedRecipesCache() {
    if (!cache.getRecipeBook().recipes.any { it.isEncryptionEnabled && !it.isEncrypted }) return
    localSource.getRecipeBook().onSuccess { recipes -> cache.setRecipeBook(recipes) }
  }

  private suspend fun deleteLocalEncryptedRecipes() {
    localSource.getRecipeBook().onSuccess { recipes ->
      recipes.forEach { recipe ->
        if (recipe.isEncryptionEnabled) localCrudSource.deleteRecipe(recipe.id)
      }
      cache.setRecipeBook(recipes.filter { !it.isEncryptionEnabled })
    }
  }

  override suspend fun clearLocalData(exceptProfileId: String?): EmptyResult {
    syncDataJob?.cancel()?.also { syncDataJob = null }
    updateEncryptionStateJob?.cancel().also { updateEncryptionStateJob = null }

    val result = localSource.clearData(exceptProfileId)
    cache.clear()

    return result
  }

  companion object {
    private const val REFRESH_TIME_THRESHOLD = 5 * 60 * 1000L
  }
}

private fun RecipeInfo.withState(state: RecipeState): RecipeInfo {
  val meta = meta.copy(
    owner = ProfileInfo(
      id = meta.owner.id,
      name = state.ownerName,
      avatar = state.ownerAvatar,
    ),
    rating = RecipeMeta.Rating(
      index = state.rating.index,
      score = state.rating.score,
      votes = state.rating.votes,
    ),
    version = state.version,
  )
  return when (this) {
    is RecipeInfo.Decrypted -> copy(
      meta = meta,
      preview = preview,
      categories = state.categories,
      isFavourite = state.isFavourite,
    )

    is RecipeInfo.Encrypted -> copy(
      meta = meta,
      preview = preview,
      categories = state.categories,
      isFavourite = state.isFavourite,
    )
  }
}