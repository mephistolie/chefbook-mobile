package com.cactusknights.chefbook.repositories

import com.cactusknights.chefbook.domain.ContentRepository
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.repositories.sync.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.net.URI
import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val authSource: SyncAuthRepository,
    private val usersSource: SyncUsersRepository,
    private val encryptionRepository: SyncEncryptionRepository,
    private val recipesSource: SyncRecipesRepository,
    private val categoriesSource: SyncCategoriesRepository,
    private val shoppingListSource: SyncShoppingListRepository,
    private val settingsRepository: SyncSettingsRepository,
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
    override suspend fun getUserInfo(): User { encryptionRepository.testEncryption(); return usersSource.getUserInfo() }
    override suspend fun changeName(username: String) { return usersSource.changeName(username) }
    override suspend fun uploadAvatar(uri: String) { return usersSource.uploadAvatar(uri) }
    override suspend fun deleteAvatar() { return usersSource.deleteAvatar() }

    override suspend fun listenToUserRecipes(): StateFlow<List<Recipe>> { return recipesSource.listenToUserRecipes() }
    override suspend fun getRecipes(): List<Recipe> { return recipesSource.getRecipes() }
    override suspend fun addRecipe(recipe: Recipe) : Recipe { return recipesSource.addRecipe(recipe) }
    override suspend fun getRecipe(recipeId: Int): Recipe { return recipesSource.getRecipe(recipeId)}
    override suspend fun getRecipeByRemoteId(remoteId: Int): Recipe { return recipesSource.getRecipeByRemoteId(remoteId) }
    override suspend fun updateRecipe(recipe: Recipe): Recipe { return recipesSource.updateRecipe(recipe) }
    override suspend fun deleteRecipe(recipe: Recipe) { recipesSource.deleteRecipe(recipe) }
    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) { return recipesSource.setRecipeFavouriteStatus(recipe) }
    override suspend fun setRecipeCategories(recipe: Recipe) { return recipesSource.setRecipeCategories(recipe) }

    override suspend fun setRecipeLikeStatus(recipe: Recipe) { return recipesSource.setRecipeLikeStatus(recipe) }

    override suspend fun listenToCategories(): StateFlow<List<Category>> { return categoriesSource.listenToCategories() }
    override suspend fun getCategories(): List<Category> { return categoriesSource.getCategories() }
    override suspend fun addCategory(category: Category) : Int { return categoriesSource.addCategory(category) }
    override suspend fun getCategory(categoryId: Int): Category { return categoriesSource.getCategory(categoryId) }
    override suspend fun updateCategory(category: Category) { categoriesSource.updateCategory(category) }
    override suspend fun deleteCategory(category: Category) { categoriesSource.deleteCategory(category) }

    override suspend fun listenToShoppingList(): StateFlow<ShoppingList> { return shoppingListSource.listenToShoppingList() }
    override suspend fun syncShoppingList() { shoppingListSource.syncShoppingList() }
    override suspend fun getShoppingList(): ShoppingList { return shoppingListSource.getShoppingList() }
    override suspend fun setShoppingList(shoppingList: ShoppingList) { shoppingListSource.setShoppingList(shoppingList) }
    override suspend fun addToShoppingList(purchases: List<Purchase>) { shoppingListSource.addToShoppingList(purchases) }

    override fun getSettings(): Settings { return settingsRepository.getSettings() }
    override fun getShoppingListByDefault(): Boolean { return settingsRepository.getShoppingListByDefault() }
    override suspend fun setShoppingListByDefault(isShoppingListDefault: Boolean) { return settingsRepository.setShoppingListByDefault(isShoppingListDefault) }
    override fun getDataSourceType(): DataSource { return settingsRepository.getDataSourceType() }
    override suspend fun setDataSourceType(dataSource: DataSource) { return settingsRepository.setDataSourceType(dataSource) }
    override fun getUserType(): UserType { return settingsRepository.getUserType() }
    override suspend fun setUserType(userType: UserType) { return settingsRepository.setUserType(userType) }
    override fun getAppTheme(): AppTheme { return settingsRepository.getAppTheme() }
    override suspend fun setAppTheme(appTheme: AppTheme) { return settingsRepository.setAppTheme(appTheme) }
    override fun getAppIcon(): AppIcon { return settingsRepository.getAppIcon() }
    override suspend fun setAppIcon(appIcon: AppIcon) { return settingsRepository.setAppIcon(appIcon) }
}