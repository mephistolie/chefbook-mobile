package com.cactusknights.chefbook.domain

import com.cactusknights.chefbook.core.cache.CategoriesCacheReader
import com.cactusknights.chefbook.core.cache.RecipeBookCacheReader
import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.data.*
import kotlinx.coroutines.flow.StateFlow
import javax.crypto.SecretKey

interface AuthRepo : AuthDataSource {
    suspend fun signInLocally()
}

interface ProfileRepo : RemoteProfileDataSource {
    suspend fun listenToProfile(): StateFlow<Profile?>
}

interface VaultEncryptionRepo {
    suspend fun listenToUnlockedState(): StateFlow<Boolean>
    suspend fun getEncryptionState(): EncryptionState
    suspend fun createEncryptedVault(password: String)
    suspend fun unlockEncryptedVault(password: String)
    suspend fun lockEncryptedVault()
    suspend fun deleteEncryptedVault()
}

interface RecipeBookSyncRepo : RecipeBookCacheReader {
    suspend fun syncRecipeBook()
}

interface RecipeCrudRepo {
    suspend fun getRecipeByRemoteId(remoteId: Int): Recipe
    suspend fun createRecipe(recipe: DecryptedRecipe): Recipe
    suspend fun getRecipeById(recipeId: Int): Recipe
    suspend fun updateRecipe(recipe: DecryptedRecipe): Recipe
    suspend fun deleteRecipe(recipe: Recipe)
}

interface RecipePicturesRepo {
    suspend fun syncRecipePictures(recipe: DecryptedRecipe, key: SecretKey?): DecryptedRecipe
}

interface RecipeInteractionRepo {
    suspend fun syncRecipeInteractions(local: RecipeInfo, remote: RecipeInfo) : RecipeInfo
    suspend fun setRecipeLikeStatus(recipe: Recipe)
    suspend fun setRecipeFavouriteStatus(recipe: Recipe)
    suspend fun setRecipeCategories(recipe: Recipe)
    suspend fun setRecipeRemoteCategories(recipe: Recipe)
}

interface RecipeEncryptionRepo {
    suspend fun syncRecipeKey(localId: Int, remoteId: Int)
    suspend fun setRecipeKey(localId: Int, remoteId: Int?, recipeKey: SecretKey)
    suspend fun getRecipeKey(localId: Int, remoteId: Int?): SecretKey
    suspend fun decryptRecipeData(localId: Int, remoteId: Int?, encryptedData: ByteArray): ByteArray
    suspend fun deleteRecipeKey(localId: Int, remoteId: Int?)
}

interface CategoriesSyncRepo : CategoriesCacheReader {
    suspend fun syncCategories()
}

interface CategoriesCrudRepo {
    suspend fun addCategory(category: Category): Int
    suspend fun getCategory(categoryId: Int): Category
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}

interface ShoppingListRepo : ShoppingListDataSource {
    suspend fun listenToShoppingList(): StateFlow<ShoppingList?>
    suspend fun syncShoppingList()
}