package com.mysty.chefbook.api.recipe.data.repositories

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.communication.safeData
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.profile.domain.IProfileRepo
import com.mysty.chefbook.api.recipe.data.cache.IRecipeBookCache
import com.mysty.chefbook.api.recipe.domain.IRecipeEncryptionRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.RecipesFilter
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.toRecipe
import com.mysty.chefbook.api.sources.domain.IRecipeCryptor
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import com.mysty.chefbook.core.coroutines.AppDispatchers
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.crypto.SecretKey
import kotlin.math.abs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class RecipeRepo(
  private val localSource: ILocalRecipeSource,
  private val remoteSource: IRemoteRecipeSource,
  private val localInteractionSource: ILocalRecipeInteractionSource,

  private val cache: IRecipeBookCache,
  private val encryptedVaultRepo: IEncryptedVaultRepo,
  private val recipeEncryptionRepo: IRecipeEncryptionRepo,
  private val categoriesRepo: ICategoryRepo,
  private val profileRepo: IProfileRepo,
  private val source: ISourcesRepo,
  private val cryptor: IRecipeCryptor,
  private val dispatchers: AppDispatchers,
  private val scopes: CoroutineScopes,
) : IRecipeRepo {

  private var refreshTimestamp: Long = 0

  init {
    observeEncryptedVaultState()
  }

  override fun observeRecipeBook(): Flow<List<RecipeInfo>?> {
    scopes.repository.launch { refreshData() }
    return cache.observeRecipeBook()
  }

  override suspend fun getRecipeBook(forceRefresh: Boolean): List<RecipeInfo> {
    refreshData(forceRefresh)
    return cache.getRecipeBook()
  }

  override suspend fun refreshRecipeBook() {
    refreshData(force = true)
  }

  override suspend fun getRecipesByQuery(query: RecipesFilter) =
    if (source.useRemoteSource()) {
      remoteSource.getRecipesByQuery(query)
    } else {
      Failure(AppError(AppErrorType.LOCAL_USER))
    }

  private suspend fun refreshData(force: Boolean = false) {
    if (source.useRemoteSource()) {
      if (force || abs(System.currentTimeMillis() - refreshTimestamp) > REFRESH_TIME_THRESHOLD) {

        val localResult = localSource.getRecipeBook()
        if (localResult.isSuccess()) cache.setRecipeBook(localResult.data())

        val remoteResult = remoteSource.getRecipeBook()
        if (remoteResult.isSuccess()) {
          cache.setRecipeBook(remoteResult.data())
          pullChanges(
            if (localResult.isSuccess()) localResult.data() else emptyList(),
            remoteResult.data()
          )
        }

        refreshTimestamp = System.currentTimeMillis()
      }
    } else {
      val localResult = localSource.getRecipeBook()
      if (localResult.isSuccess()) cache.setRecipeBook(localResult.data())
    }
  }

  private suspend fun pullChanges(local: List<RecipeInfo>, remote: List<RecipeInfo>) {
    categoriesRepo.getCategories()
    for (remoteRecipe in remote) {
      val localRecipe = local.find { it.id == remoteRecipe.id }
      if (localRecipe == null) {
        val result = remoteSource.getRecipe(remoteRecipe.id)
        if (result.isFailure()) continue
        localSource.createRecipe(result.data())
      } else if (remoteRecipe.creationTimestamp < localRecipe.creationTimestamp) {
        val result = remoteSource.getRecipe(remoteRecipe.id)
        if (result.isFailure()) continue
        localSource.updateRecipe(result.data())
      }
      pullRecipeInteractions(localRecipe, remoteRecipe)
    }

    val remoteIds = remote.map { it.id }
    val deletedRecipes = local.filter { it.id !in remoteIds }
    for (recipe in deletedRecipes) {
      localSource.deleteRecipe(recipe.id)
    }
  }

  private suspend fun pullRecipeInteractions(local: RecipeInfo?, remote: RecipeInfo) {
    if (local?.isLiked != remote.isLiked) {
      localInteractionSource.setRecipeLikeStatus(remote.id, remote.isLiked)
    }
    if (local?.likes != remote.likes) {
      localInteractionSource.setRecipeLikes(remote.id, remote.likes)
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

  override suspend fun createRecipe(input: RecipeInput, key: SecretKey?): ActionStatus<Recipe> {
    val owner = (profileRepo.getProfile() as? Successful)?.data

    var result: ActionStatus<Recipe> = if (source.useRemoteSource()) {

      val remoteResult = remoteSource.createRecipe(input)
      if (remoteResult.isSuccess()) {
        val newRecipe =
          input.toRecipe(id = remoteResult.data(), ownerId = owner?.id, ownerName = owner?.username)
        localSource.createRecipe(newRecipe)
        DataResult(newRecipe)
      } else remoteResult.asFailure()

    } else {

      val localResult =
        localSource.createRecipe(input.toRecipe(ownerId = owner?.id, ownerName = owner?.username))
      if (localResult.isSuccess()) {
        DataResult(
          input.toRecipe(
            id = localResult.data(),
            ownerId = owner?.id,
            ownerName = owner?.username
          )
        )
      } else localResult.asFailure()

    }

    if (result is Successful) {
      var recipe = result.data
      if (recipe.encryptionState is EncryptionState.Encrypted && key != null) {
        recipe = cryptor.decryptRecipe(recipe, key)
      }
      cache.putRecipe(recipe)
      result = DataResult(recipe)
    }

    return decryptResult(result)
  }

  override suspend fun observeRecipe(recipeId: String): Flow<Recipe?> {
    if (cache.getRecipe(recipeId) == null) {
      val result = getRecipe(recipeId)
      if (result.isFailure()) throw result.asFailure().error ?: AppError(AppErrorType.UNKNOWN_ERROR)
    }
    return cache.observeRecipe(recipeId)
  }

  override suspend fun getRecipe(recipeId: String): ActionStatus<Recipe> {
    cache.getRecipe(recipeId)?.let { return DataResult(it) }

    var result = localSource.getRecipe(recipeId)
    if (result.isFailure() && source.useRemoteSource()) {
      result = remoteSource.getRecipe(recipeId)
    }

    if (result.isSuccess()) {
      var recipe = result.data()
      if (recipe.encryptionState is EncryptionState.Encrypted) {
        val userKey = encryptedVaultRepo.getUserPrivateKey()
        if (userKey.isFailure()) return userKey.asFailure()

        val recipeKey = recipeEncryptionRepo.getRecipeKey(recipeId, userKey.data())
        if (recipeKey.isFailure()) return recipeKey.asFailure()

        recipe = cryptor.decryptRecipe(recipe, recipeKey.data())
      }
      result = DataResult(recipe)
      cache.putRecipe(recipe)
    }
    return result
  }

  override suspend fun updateRecipe(
    recipeId: String,
    input: RecipeInput,
    key: SecretKey?
  ): ActionStatus<Recipe> {
    val unmodifiedRecipe = (getRecipe(recipeId) as? Successful)?.data

    if (source.useRemoteSource()) {
      val result = remoteSource.updateRecipe(recipeId, input)
      if (result.isFailure()) return result.asFailure()
    }

    var updatedRecipe = input.toRecipe(
      id = recipeId,
      ownerId = unmodifiedRecipe?.ownerId,
      ownerName = unmodifiedRecipe?.ownerName,
      isSaved = unmodifiedRecipe?.isSaved ?: true,
      likes = unmodifiedRecipe?.likes,
      creationTimestamp = unmodifiedRecipe?.creationTimestamp ?: LocalDateTime.now(ZoneOffset.UTC),
      isFavourite = unmodifiedRecipe?.isFavourite ?: false,
      isLiked = unmodifiedRecipe?.isLiked ?: false,
    )

    localSource.updateRecipe(updatedRecipe)

    if (updatedRecipe.encryptionState is EncryptionState.Encrypted && key != null) {
      updatedRecipe = cryptor.decryptRecipe(updatedRecipe, key)
    }
    cache.putRecipe(updatedRecipe)

    return DataResult(updatedRecipe)
  }

  private suspend fun decryptResult(uploadResult: ActionStatus<Recipe>): ActionStatus<Recipe> {
    if (uploadResult.isFailure()) return uploadResult
    var recipe = uploadResult.data()
    if (recipe.encryptionState is EncryptionState.Encrypted) {
      val decryptionResult = decryptRecipe(recipe)
      if (decryptionResult.isFailure()) return decryptionResult.asFailure()
      recipe = decryptionResult.data()
    }
    cache.putRecipe(recipe)
    return DataResult(recipe)
  }

  override suspend fun deleteRecipe(recipeId: String): SimpleAction {
    if (source.useRemoteSource()) {
      val result = remoteSource.deleteRecipe(recipeId)
      if (result.isFailure()) return result.asFailure()
    }

    localSource.deleteRecipe(recipeId)
    cache.removeRecipe(recipeId)

    return SuccessResult
  }

  private fun observeEncryptedVaultState() {
    scopes.repository.launch {
      encryptedVaultRepo.observeEncryptedVaultState()
        .collect { state ->
          if (state is EncryptedVaultState.Unlocked) decryptRecipes(state.keys.private) else encryptRecipes()
        }
    }
  }

  private suspend fun decryptRecipes(key: PrivateKey) = withContext(dispatchers.default) {
    val recipeBook = cache.getRecipeBook()
    cache.setRecipeBook(recipeBook.map { recipe ->
      if (recipe.encryptionState != EncryptionState.Encrypted) return@map recipe
      decryptRecipe(recipe, key)
    })
  }

  private suspend fun decryptRecipe(recipe: Recipe): ActionStatus<Recipe> {
    val userKeyResult = encryptedVaultRepo.getUserPrivateKey()
    if (userKeyResult.isFailure()) return userKeyResult.asFailure()

    val recipeKey = getRecipeKey(recipe.id, key = userKeyResult.data()) ?: return Failure(
      AppError(
        AppErrorType.UNABLE_DECRYPT
      )
    )
    val decryptedRecipe = cryptor.decryptRecipe(recipe, recipeKey)
    return DataResult(decryptedRecipe)
  }

  private suspend fun decryptRecipe(recipe: RecipeInfo, key: PrivateKey): RecipeInfo {
    val recipeKey = getRecipeKey(recipe.id, key) ?: return recipe
    return cryptor.decryptRecipe(recipe, recipeKey)
  }

  private suspend fun encryptRecipes() {
    val recipeBook = cache.getRecipeBook()
    cache.setRecipeBook(recipeBook.map(cryptor::encryptRecipe))
  }

  private suspend fun getRecipeKey(recipeId: String, key: PrivateKey) =
    recipeEncryptionRepo.getRecipeKey(recipeId, key).safeData()

  companion object {
    private const val REFRESH_TIME_THRESHOLD = 5 * 60 * 1000
  }

}
