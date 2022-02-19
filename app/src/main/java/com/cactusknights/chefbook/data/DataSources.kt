package com.cactusknights.chefbook.data

import com.cactusknights.chefbook.models.*
import java.util.*

interface AuthDataSource {
    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signOut()
}

interface ProfileDataSource {
    suspend fun getProfileInfo(): Profile
}

interface LocalProfileDataSource : ProfileDataSource {
    suspend fun getCachedProfileInfo() : Profile
    suspend fun cacheProfileInfo(info: Profile)
}

interface RemoteProfileDataSource : ProfileDataSource {
    suspend fun changeName(username: String)
    suspend fun uploadAvatar(uriString: String)
    suspend fun deleteAvatar()
}

interface EncryptionDataSource {
    suspend fun getEncryptedUserKey(): ByteArray?
    suspend fun setEncryptedUserKey(data: ByteArray)
    suspend fun deleteEncryptedUserKey()
    suspend fun getEncryptedRecipeKey(recipeId: Int): ByteArray?
    suspend fun setEncryptedRecipeKey(recipeId: Int, key: ByteArray)
    suspend fun deleteEncryptedRecipeKey(recipeId: Int)
}

interface RecipeCrudDataSource {
    suspend fun getRecipeBook(): List<RecipeInfo>
    suspend fun createRecipe(recipe: Recipe): Recipe
    suspend fun getRecipeById(recipeId: Int): Recipe
    suspend fun updateRecipe(recipe: Recipe): Recipe
    suspend fun deleteRecipe(recipeId: Int)
}
interface LocalRecipeBookDataSource : RecipeCrudDataSource {
    suspend fun getRecipeByRemoteId(remoteId: Int): Recipe?
    suspend fun getDeletedRecipeRemoteIds(): List<Int>
}

interface RecipeInteractionDataSource {
    suspend fun setRecipeLikeStatus(recipeId: Int, isLiked: Boolean)
    suspend fun setRecipeFavouriteStatus(recipeId: Int, isFavourite: Boolean)
    suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>)
}

interface LocalRecipeInteractionDataSource : RecipeInteractionDataSource {
    suspend fun updateRecipeInteractionTimestamp(recipeId: Int, timestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis))
}

interface RecipePicturesDataSource {
    suspend fun getPicturesUri(recipeId: Int): List<String>
    suspend fun addPicture(recipeId: Int, data: ByteArray): String
    suspend fun deletePicture(recipeId: Int, name: String)
}

interface CategoriesDataSource {
    suspend fun getCategories(): List<Category>
    suspend fun addCategory(category: Category): Int
    suspend fun getCategory(categoryId: Int): Category
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(categoryId: Int)
}

interface LocalCategoriesDataSource : CategoriesDataSource {
    suspend fun getDeletedCategoriesRemoteIds(): List<Int>
}

interface ShoppingListDataSource {
    suspend fun getShoppingList(): ShoppingList
    suspend fun setShoppingList(shoppingList: ShoppingList)
    suspend fun addToShoppingList(purchases: List<Purchase>)
}