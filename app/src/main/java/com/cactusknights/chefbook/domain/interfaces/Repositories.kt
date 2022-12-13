package com.cactusknights.chefbook.domain.interfaces

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.Tokens
import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.domain.entities.profile.Profile
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.RecipesFilter
import com.cactusknights.chefbook.domain.entities.settings.Icon
import com.cactusknights.chefbook.domain.entities.settings.Mode
import com.cactusknights.chefbook.domain.entities.settings.Settings
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.domain.entities.settings.Theme
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ISettingsRepo {
    suspend fun observeSettings(): Flow<Settings>
    suspend fun getSettings(): Settings
    suspend fun getAppMode(): Mode
    suspend fun setAppMode(mode: Mode)
    suspend fun getAppTheme(): Theme
    suspend fun setAppTheme(theme: Theme)
    suspend fun getAppIcon(): Icon
    suspend fun setAppIcon(icon: Icon)
    suspend fun getDefaultTab(): Tab
    suspend fun setDefaultTab(tab: Tab)
    suspend fun checkFirstAppLaunch(): Boolean
    suspend fun getDefaultRecipeLanguage(): Language
    suspend fun setDefaultRecipeLanguage(language: Language)
    suspend fun getOnlineRecipesLanguages(): List<String>
    suspend fun setOnlineRecipesLanguages(languages: List<String>)
}

interface ISourceRepo {
    suspend fun observeServerAccess(): StateFlow<Boolean>
    suspend fun isOnlineMode(): Boolean
    suspend fun useRemoteSource(): Boolean
    suspend fun setServerAccess(hasConnection: Boolean)
    suspend fun clearLocalData()
}

interface ISessionRepo {
    suspend fun observeSession(): MutableStateFlow<Tokens?>
    fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(tokens: Tokens)
    suspend fun clearTokens()
}

interface IAuthRepo {
    suspend fun signUp(email: String, password: String): SimpleAction
    suspend fun signIn(email: String, password: String): ActionStatus<Tokens>
    suspend fun signOut(refreshToken: String): SimpleAction
}

interface IProfileRepo {
    suspend fun observeProfile(): StateFlow<Profile?>
    suspend fun getProfile(forceRefresh: Boolean = false): ActionStatus<Profile>
    suspend fun changeUsername(username: String): SimpleAction
    suspend fun changePassword(oldPassword: String, newPassword: String): SimpleAction
    suspend fun uploadAvatar(path: String): SimpleAction
    suspend fun deleteAvatar(): SimpleAction
    suspend fun clearLocalData()
}

interface IEncryptedVaultRepo {
    suspend fun observeEncryptedVaultState(): StateFlow<EncryptedVaultState>
    suspend fun getEncryptedVaultState(): EncryptedVaultState
    suspend fun createEncryptedVault(password: String, salt: ByteArray): SimpleAction
    suspend fun unlockEncryptedVault(password: String, salt: ByteArray): SimpleAction
    suspend fun lockEncryptedVault(): SimpleAction
    suspend fun getUserPublicKey(): ActionStatus<PublicKey>
    suspend fun getUserPrivateKey(): ActionStatus<PrivateKey>
    suspend fun deleteEncryptedVault(): SimpleAction
}

interface IRecipeRepo {
    suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?>
    suspend fun getRecipeBook(forceRefresh: Boolean = false): List<RecipeInfo>
    suspend fun getRecipesByQuery(query: RecipesFilter): ActionStatus<List<RecipeInfo>>
    suspend fun createRecipe(input: RecipeInput, key: SecretKey?): ActionStatus<Recipe>
    suspend fun getRecipe(recipeId: String): ActionStatus<Recipe>
    suspend fun cacheRecipe(recipe: Recipe): SimpleAction
    suspend fun updateRecipe(recipeId: String, input: RecipeInput, key: SecretKey?): ActionStatus<Recipe>
    suspend fun deleteRecipe(recipeId: String): SimpleAction
}

interface IRecipePictureRepo {
    suspend fun uploadRecipePictures(
        recipeId: String,
        input: RecipeInput,
        key: SecretKey?,
        isEncrypted: Boolean = key != null,
        wasEncrypted: Boolean = false
    ): RecipeInput
}

interface IRecipeInteractionRepo {
    suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean): SimpleAction
    suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean): SimpleAction
    suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean): SimpleAction
    suspend fun setRecipeCategories(recipeId: String, categories: List<String>): SimpleAction
}

interface IRecipeEncryptionRepo {
    suspend fun getRecipeKey(recipeId: String, userKey: PrivateKey): ActionStatus<SecretKey>
    suspend fun setRecipeKey(recipeId: String, recipeKey: SecretKey, userKey: PublicKey): SimpleAction
    suspend fun deleteRecipeKey(recipeId: String): SimpleAction
}

interface ILatestRecipesRepo {
    suspend fun observeLatestRecipes(): Flow<List<String>>
    suspend fun getLatestRecipes(): List<String>
    suspend fun pushRecipe(recipeId: String)
}

interface ICategoryRepo {
    suspend fun observeCategories(): StateFlow<List<Category>?>
    suspend fun getCategories(forceRefresh: Boolean = false): List<Category>
    suspend fun createCategory(input: CategoryInput): ActionStatus<Category>
    suspend fun getCategory(categoryId: String): ActionStatus<Category>
    suspend fun updateCategory(categoryId: String, input: CategoryInput): ActionStatus<Category>
    suspend fun deleteCategory(categoryId: String): SimpleAction
}

interface IShoppingListRepo {
    suspend fun observeShoppingList(): StateFlow<ShoppingList>
    suspend fun getShoppingList(forceRefresh: Boolean = false): ActionStatus<ShoppingList>
    suspend fun syncShoppingList()
    suspend fun setShoppingList(purchases: List<Purchase>): SimpleAction
    suspend fun switchPurchaseStatus(purchaseId: String): SimpleAction
    suspend fun removePurchasedItems(): SimpleAction
    suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction
}