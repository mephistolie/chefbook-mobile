package com.cactusknights.chefbook.repositories

import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.domain.ContentRepository
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.repositories.sync.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository @Inject constructor(
    private val authSource: SyncAuthRepository,
    private val usersSource: SyncUsersRepository,
    private val encryptionRepository: SyncEncryptionRepository,
    private val recipesSource: SyncRecipesRepository,
    private val categoriesSource: SyncCategoriesRepository,
    private val shoppingListSource: SyncShoppingListRepository,
) : ContentRepository {

    override suspend fun signUp(email: String, password: String) { authSource.signUp(email, password) }
    override suspend fun signIn(email: String, password: String): Boolean {
        val signedIn = authSource.signIn(email, password)
        if (signedIn) CoroutineScope(Dispatchers.IO).launch { recipesSource.getRecipes() }
        return signedIn
    }

    override suspend fun signInLocally() { authSource.signInLocally() }
    override suspend fun signOut() { authSource.signOut(); usersSource.getUserInfo() }

    override suspend fun listenToUser(): StateFlow<User?> { return usersSource.listenToUser() }
    override suspend fun getUserInfo(): User { return usersSource.getUserInfo() }
    override suspend fun changeName(username: String) { return usersSource.changeName(username) }
    override suspend fun uploadAvatar(uri: String) { return usersSource.uploadAvatar(uri) }
    override suspend fun deleteAvatar() { return usersSource.deleteAvatar() }

    override suspend fun listenToUserRecipes(): StateFlow<List<Recipe>?> { return recipesSource.listenToUserRecipes() }
    override suspend fun getRecipes(): List<Recipe> { return recipesSource.getRecipes() }
    override suspend fun addRecipe(recipe: Recipe) : Recipe { return recipesSource.addRecipe(recipe) }
    override suspend fun getRecipe(recipeId: Int): Recipe { return recipesSource.getRecipe(recipeId)}
    override suspend fun getRecipeByRemoteId(remoteId: Int): Recipe { return recipesSource.getRecipeByRemoteId(remoteId) }
    override suspend fun updateRecipe(recipe: Recipe): Recipe { return recipesSource.updateRecipe(recipe) }
    override suspend fun deleteRecipe(recipe: Recipe) { recipesSource.deleteRecipe(recipe) }
    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) { return recipesSource.setRecipeFavouriteStatus(recipe) }
    override suspend fun setRecipeCategories(recipe: Recipe) { return recipesSource.setRecipeCategories(recipe) }

    override suspend fun setRecipeLikeStatus(recipe: Recipe) { return recipesSource.setRecipeLikeStatus(recipe) }

    override suspend fun listenToCategories(): StateFlow<List<Category>?> { return categoriesSource.listenToCategories() }
    override suspend fun getCategories(): List<Category> { return categoriesSource.getCategories() }
    override suspend fun addCategory(category: Category) : Int { return categoriesSource.addCategory(category) }
    override suspend fun getCategory(categoryId: Int): Category { return categoriesSource.getCategory(categoryId) }
    override suspend fun updateCategory(category: Category) { categoriesSource.updateCategory(category) }
    override suspend fun deleteCategory(category: Category) { categoriesSource.deleteCategory(category) }

    override suspend fun listenToShoppingList(): StateFlow<ShoppingList?> { return shoppingListSource.listenToShoppingList() }
    override suspend fun syncShoppingList() { shoppingListSource.syncShoppingList() }
    override suspend fun getShoppingList(): ShoppingList { return shoppingListSource.getShoppingList() }
    override suspend fun setShoppingList(shoppingList: ShoppingList) { shoppingListSource.setShoppingList(shoppingList) }
    override suspend fun addToShoppingList(purchases: List<Purchase>) { shoppingListSource.addToShoppingList(purchases) }

    override suspend fun listenToUnlockedState(): StateFlow<Boolean> { return encryptionRepository.listenToUnlockedState() }
    override suspend fun getEncryptionState() : EncryptionState { return encryptionRepository.getEncryptionState() }
    override suspend fun createEncryptedVault(password: String) { return encryptionRepository.createEncryptedVault(password) }
    override suspend fun unlockEncryptedVault(password: String) { return encryptionRepository.unlockEncryptedVault(password) }
    override suspend fun lockEncryptedVault() { return encryptionRepository.lockEncryptedVault() }
    override suspend fun deleteEncryptedVault() { return encryptionRepository.deleteEncryptedVault() }
    override suspend fun setRecipeKey(localId: Int, remoteId: Int?, recipeKey: SecretKey) { return encryptionRepository.setRecipeKey(localId, remoteId, recipeKey) }
    override suspend fun getRecipeKey(localId: Int, remoteId: Int?): SecretKey { return encryptionRepository.getRecipeKey(localId, remoteId) }
    override suspend fun decryptRecipeData(localId: Int, remoteId: Int?, encryptedData: ByteArray): ByteArray { return encryptionRepository.decryptRecipeData(localId, remoteId, encryptedData) }
    override suspend fun deleteRecipeKey(localId: Int, remoteId: Int?) { return encryptionRepository.deleteRecipeKey(localId, remoteId) }

}