package com.cactusknights.chefbook.domain

import com.cactusknights.chefbook.models.*
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.net.URI
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey

interface AuthDataSource {
    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signOut()
}

interface UserDataSource {
    suspend fun getUserInfo(): User
}

interface ServerUserDataSource : UserDataSource {
    suspend fun changeName(username: String)
    suspend fun uploadAvatar(uri: String)
    suspend fun deleteAvatar()
}

interface RecipesDataSource {
    suspend fun getRecipes(): List<Recipe>
    suspend fun addRecipe(recipe: Recipe) : Recipe
    suspend fun getRecipe(recipeId: Int) : Recipe
    suspend fun updateRecipe(recipe: Recipe): Recipe
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun setRecipeFavouriteStatus(recipe: Recipe)
    suspend fun setRecipeCategories(recipe: Recipe)
}

interface EncryptionDataStore {
    suspend fun getEncryptedUserKey(): ByteArray?
    suspend fun setEncryptedUserKey(encryptedRsa: ByteArray): Boolean
    suspend fun getEncryptedRecipeKeys(recipeId: Int): ByteArray?
    suspend fun setEncryptedRecipeKeys(recipeId: Int, encryptedRsa: ByteArray): Boolean
}

interface EncryptionRepository {
    suspend fun getRecipe(recipeId: Int)
    suspend fun setAesKey(keyphrase: String)
    suspend fun generateRecipeRSA(recipeId: Int)
    suspend fun setRecipeRsaPrivateKey(recipeId: Int)
}

interface ServerRecipeDataSource : RecipesDataSource {
    suspend fun setRecipeLikeStatus(recipe: Recipe)
}

interface CategoriesDataSource {
    suspend fun getCategories(): List<Category>
    suspend fun addCategory(category: Category) : Int
    suspend fun getCategory(categoryId: Int) : Category
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}

interface ShoppingListDataSource {
    suspend fun getShoppingList(): ShoppingList
    suspend fun setShoppingList(shoppingList: ShoppingList)
    suspend fun addToShoppingList(purchases: List<Purchase>)
}

interface SettingsRepository {
    fun getSettings() : Settings
    fun getShoppingListByDefault() : Boolean
    suspend fun setShoppingListByDefault(isShoppingListDefault: Boolean)
    fun getDataSourceType() : DataSource
    suspend fun setDataSourceType(dataSource: DataSource)
    fun getUserType() : UserType
    suspend fun setUserType(userType: UserType)
    fun getAppTheme() : AppTheme
    suspend fun setAppTheme(appTheme: AppTheme)
    fun getAppIcon() : AppIcon
    suspend fun setAppIcon(appIcon: AppIcon)
}

interface AuthRepository: AuthDataSource {
    suspend fun signInLocally()
}

interface UserRepository: ServerUserDataSource {
    suspend fun listenToUser(): StateFlow<User?>
}

interface RecipesRepository: ServerRecipeDataSource {
    suspend fun listenToUserRecipes(): StateFlow<List<Recipe>>
    suspend fun getRecipeByRemoteId(remoteId: Int) : Recipe
}

interface CategoriesRepository: CategoriesDataSource {
    suspend fun listenToCategories(): StateFlow<List<Category>>
}

interface ShoppingListRepository: ShoppingListDataSource {
    suspend fun listenToShoppingList(): StateFlow<ShoppingList>
    suspend fun syncShoppingList()
}

interface ContentRepository: AuthRepository, UserRepository, RecipesRepository, CategoriesRepository, ShoppingListRepository, SettingsRepository