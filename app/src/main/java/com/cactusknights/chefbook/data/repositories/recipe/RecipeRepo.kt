package com.cactusknights.chefbook.data.repositories.recipe

import com.cactusknights.chefbook.core.coroutines.AppDispatchers
import com.cactusknights.chefbook.core.coroutines.CoroutineScopes
import com.cactusknights.chefbook.data.ILocalRecipeInteractionSource
import com.cactusknights.chefbook.data.ILocalRecipeSource
import com.cactusknights.chefbook.data.IRemoteRecipeSource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.action.Successful
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.RecipesFilter
import com.cactusknights.chefbook.domain.entities.recipe.decrypt
import com.cactusknights.chefbook.domain.entities.recipe.encrypt
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState
import com.cactusknights.chefbook.domain.entities.recipe.toRecipe
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.IProfileRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCache
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import com.cactusknights.chefbook.domain.interfaces.ISourceRepo
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.spongycastle.util.encoders.Base64
import timber.log.Timber
import kotlin.math.abs

@Singleton
class RecipeRepo @Inject constructor(
    @Local private val localSource: ILocalRecipeSource,
    @Remote private val remoteSource: IRemoteRecipeSource,
    @Local private val localInteractionSource: ILocalRecipeInteractionSource,

    private val cache: IRecipeBookCache,
    private val encryptedVaultRepo: IEncryptedVaultRepo,
    private val recipeEncryptionRepo: IRecipeEncryptionRepo,
    private val categoriesRepo: ICategoryRepo,
    private val profileRepo: IProfileRepo,
    private val source: ISourceRepo,
    private val cryptor: ICryptor,
    private val dispatchers: AppDispatchers,
    private val scopes: CoroutineScopes,
) : IRecipeRepo {

    private var refreshTimestamp: Long = 0

    init {
        observeEncryptedVaultState()
    }

    override suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?> {
        scopes.repository.launch { refreshData() }
        return cache.observeRecipeBook()
    }

    override suspend fun getRecipeBook(forceRefresh: Boolean): List<RecipeInfo> {
        refreshData(forceRefresh)
        return cache.getRecipeBook()
    }

    override suspend fun getRecipesByQuery(query: RecipesFilter) =
        if (source.useRemoteSource()) {
            remoteSource.getRecipesByQuery(query)
        } else {
            Failure(AppError(AppErrorType.LOCAL_USER))
        }

    private suspend fun refreshData(forceRefresh: Boolean = false) {
        if (source.useRemoteSource()) {
            if (forceRefresh || abs(System.currentTimeMillis() - refreshTimestamp) > REFRESH_TIME_THRESHOLD) {

                val localResult = localSource.getRecipeBook()
                if (localResult.isSuccess()) cache.emitRecipeBook(localResult.data())

                val remoteResult = remoteSource.getRecipeBook()
                if (remoteResult.isSuccess()) {
                    cache.emitRecipeBook(remoteResult.data())
                    pullChanges(if (localResult.isSuccess()) localResult.data() else emptyList(), remoteResult.data())
                }

                refreshTimestamp = System.currentTimeMillis()
            }
        } else {
            val localResult = localSource.getRecipeBook()
            if (localResult.isSuccess()) cache.emitRecipeBook(localResult.data())
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
                val newRecipe = input.toRecipe(id = remoteResult.data(), ownerId = owner?.id, ownerName = owner?.username)
                localSource.createRecipe(newRecipe)
                DataResult(newRecipe)
            } else remoteResult.asFailure()

        } else {

            val localResult = localSource.createRecipe(input.toRecipe(ownerId = owner?.id, ownerName = owner?.username))
            if (localResult.isSuccess()) {
                DataResult(input.toRecipe(id = localResult.data(), ownerId = owner?.id, ownerName = owner?.username))
            } else localResult.asFailure()

        }

        if (result is Successful) {
            var recipe = result.data
            if (recipe.encryptionState is EncryptionState.Encrypted && key != null) {
                recipe = recipe.decrypt(key) { data -> cryptor.decryptDataBySymmetricKey(Base64.decode(data), key) }
            }
            cache.putRecipe(recipe)
            result = DataResult(recipe)
        }

        return decryptResult(result)
    }

    override suspend fun getRecipe(recipeId: String): ActionStatus<Recipe> {
        cache.getRecipe(recipeId)?.let { return DataResult(it) }

        var result = localSource.getRecipe(recipeId)
        if (result.isFailure() && source.useRemoteSource()) {
            result = remoteSource.getRecipe(recipeId)
        }

        return result
    }

    override suspend fun updateRecipe(recipeId: String, input: RecipeInput, key: SecretKey?): ActionStatus<Recipe> {
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
            updatedRecipe = updatedRecipe.decrypt(key) { data -> cryptor.decryptDataBySymmetricKey(Base64.decode(data), key) }
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

    override suspend fun cacheRecipe(recipe: Recipe): SimpleAction {
        if (recipe.encryptionState is EncryptionState.Encrypted) return Failure(AppError(AppErrorType.UNABLE_DECRYPT))
        cache.putRecipe(recipe)
        return SuccessResult
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
                .onEach { state ->
                    if (state is EncryptedVaultState.Unlocked) decryptRecipes(state.keys.private) else encryptRecipes()
                }
                .collect()
        }
    }

    private suspend fun decryptRecipes(key: PrivateKey) = withContext(dispatchers.default) {
        val recipeBook = cache.getRecipeBook()
        cache.emitRecipeBook(recipeBook.map { recipe ->
            if (recipe.encryptionState != EncryptionState.Encrypted) return@map recipe
            decryptRecipe(recipe, key)
        })
    }

    private suspend fun decryptRecipe(recipe: Recipe): ActionStatus<Recipe> {
        val userKeyResult = encryptedVaultRepo.getUserPrivateKey()
        if (userKeyResult.isFailure()) return userKeyResult.asFailure()

        val recipeKey = getRecipeKey(recipe.id, key = userKeyResult.data()) ?: return Failure(AppError(AppErrorType.UNABLE_DECRYPT))
        val decryptedRecipe = recipe.decrypt(recipeKey) { data -> cryptor.decryptDataBySymmetricKey(Base64.decode(data), recipeKey) }
        return DataResult(decryptedRecipe)
    }

    private suspend fun decryptRecipe(recipe: RecipeInfo, key: PrivateKey): RecipeInfo {
        val recipeKey = getRecipeKey(recipe.id, key) ?: return recipe
        return recipe.decrypt(recipeKey) { data -> cryptor.decryptDataBySymmetricKey(Base64.decode(data), recipeKey) }
    }

    private suspend fun getRecipeKey(recipeId: String, key: PrivateKey): SecretKey? {
        val result = recipeEncryptionRepo.getRecipeKey(recipeId, key)
        return (result as? Successful)?.data
    }

    private suspend fun encryptRecipes() {
        val recipeBook = cache.getRecipeBook()
        cache.emitRecipeBook(recipeBook.map { recipe ->
            if (recipe.encryptionState !is EncryptionState.Decrypted) return@map recipe
            return@map recipe.encrypt { data ->
                Base64.toBase64String(cryptor.encryptDataBySymmetricKey(data, recipe.encryptionState.key))
            }
        })
    }

    companion object {
        private const val REFRESH_TIME_THRESHOLD = 5 * 60 * 1000
    }

}
