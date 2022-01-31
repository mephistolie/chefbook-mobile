package com.cactusknights.chefbook.domain

import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.repositories.*
import kotlinx.coroutines.flow.StateFlow
import javax.crypto.SecretKey

interface AuthRepository : AuthDataSource {
    suspend fun signInLocally()
}

interface UserRepository : ServerUserDataSource {
    suspend fun listenToUser(): StateFlow<User?>
}

interface RecipesRepository : ServerRecipeDataSource {
    suspend fun listenToUserRecipes(): StateFlow<List<Recipe>?>
    suspend fun getRecipeByRemoteId(remoteId: Int): Recipe
}

interface EncryptionRepository {
    suspend fun listenToUnlockedState(): StateFlow<Boolean>
    suspend fun getEncryptionState(): EncryptionState
    suspend fun createEncryptedVault(password: String)
    suspend fun unlockEncryptedVault(password: String)
    suspend fun lockEncryptedVault()
    suspend fun deleteEncryptedVault()

    suspend fun setRecipeKey(localId: Int, remoteId: Int?, recipeKey: SecretKey)
    suspend fun getRecipeKey(localId: Int, remoteId: Int?): SecretKey
    suspend fun decryptRecipeData(localId: Int, remoteId: Int?, encryptedData: ByteArray): ByteArray
    suspend fun deleteRecipeKey(localId: Int, remoteId: Int?)
}

interface CategoriesRepository : CategoriesDataSource {
    suspend fun listenToCategories(): StateFlow<List<Category>?>
}

interface ShoppingListRepository : ShoppingListDataSource {
    suspend fun listenToShoppingList(): StateFlow<ShoppingList?>
    suspend fun syncShoppingList()
}

interface ContentRepository : AuthRepository, UserRepository, RecipesRepository,
    CategoriesRepository, ShoppingListRepository, EncryptionRepository