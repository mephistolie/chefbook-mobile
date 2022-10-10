package com.cactusknights.chefbook.data

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import com.cactusknights.chefbook.domain.entities.common.Tokens
import com.cactusknights.chefbook.domain.entities.profile.Profile
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.RecipesFilter
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList

interface IAuthSource {
    suspend fun signUp(email: String, password: String): SimpleAction
    suspend fun signIn(email: String, password: String): ActionStatus<Tokens>
    suspend fun signOut(refreshToken: String): SimpleAction
}

interface IProfileSource {
    suspend fun getProfileInfo(): ActionStatus<Profile>
}

interface ILocalProfileSource : IProfileSource {
    suspend fun getCachedProfileInfo(): ActionStatus<Profile>
    suspend fun cacheProfileInfo(info: Profile): SimpleAction
    suspend fun deleteProfileCache()
}

interface IRemoteProfileSource : IProfileSource {
    suspend fun changeName(username: String): SimpleAction
    suspend fun changePassword(oldPassword: String, newPassword: String): SimpleAction
    suspend fun uploadAvatar(uriString: String): SimpleAction
    suspend fun deleteAvatar(): SimpleAction
}

interface IEncryptionSource {
    suspend fun setUserKey(data: ByteArray): SimpleAction
    suspend fun deleteUserKey(): SimpleAction
    suspend fun getRecipeKey(recipeId: Int): ActionStatus<ByteArray>
    suspend fun setRecipeKey(recipeId: Int, key: ByteArray): SimpleAction
    suspend fun deleteRecipeKey(recipeId: Int): SimpleAction
}

interface ILocalEncryptionSource: IEncryptionSource {
    suspend fun getUserKey(): ActionStatus<ByteArray>
}

interface IRemoteEncryptionSource: IEncryptionSource {
    suspend fun getUserKeyLink(): ActionStatus<String>
    suspend fun getUserKey(link: String): ActionStatus<ByteArray>
}

interface IFileSource {
    suspend fun getFile(path: String): ActionStatus<ByteArray>
}

interface IRecipeSource {
    suspend fun getRecipeBook(): ActionStatus<List<RecipeInfo>>
    suspend fun getRecipe(recipeId: Int): ActionStatus<Recipe>
    suspend fun deleteRecipe(recipeId: Int): SimpleAction
}

interface ILocalRecipeSource : IRecipeSource {
    suspend fun createRecipe(recipe: Recipe): ActionStatus<Int>
    suspend fun updateRecipe(recipe: Recipe): SimpleAction
}

interface IRemoteRecipeSource : IRecipeSource {
    suspend fun addRecipeToRecipeBook(recipeId: Int): SimpleAction
    suspend fun removeFromRecipeToRecipeBook(recipeId: Int): SimpleAction
    suspend fun getRecipesByQuery(query: RecipesFilter): ActionStatus<List<RecipeInfo>>
    suspend fun createRecipe(input: RecipeInput): ActionStatus<Int>
    suspend fun updateRecipe(recipeId: Int, input: RecipeInput): SimpleAction
}

interface IRecipeInteractionSource {
    suspend fun setRecipeLikeStatus(recipeId: Int, isLiked: Boolean): SimpleAction
    suspend fun setRecipeFavouriteStatus(recipeId: Int, isFavourite: Boolean): SimpleAction
    suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>): SimpleAction
}

interface ILocalRecipeInteractionSource : IRecipeInteractionSource {
    suspend fun setRecipeLikes(recipeId: Int, likes: Int?): SimpleAction
}

interface IRecipePictureSource {
    suspend fun getPictures(recipeId: Int): ActionStatus<List<String>>
    suspend fun addPicture(recipeId: Int, data: ByteArray): ActionStatus<String>
    suspend fun deletePicture(recipeId: Int, name: String): SimpleAction
}

interface ICategorySource {
    suspend fun getCategories(): ActionStatus<List<Category>>
    suspend fun getCategory(categoryId: Int): ActionStatus<Category>
    suspend fun deleteCategory(categoryId: Int): SimpleAction
}

interface ILocalCategorySource : ICategorySource {
    suspend fun createCategory(category: Category): ActionStatus<Int>
    suspend fun updateCategory(category: Category): SimpleAction
}

interface IRemoteCategorySource : ICategorySource {
    suspend fun createCategory(input: CategoryInput): ActionStatus<Int>
    suspend fun updateCategory(categoryId: Int, input: CategoryInput): SimpleAction
}

interface IShoppingListSource {
    suspend fun getShoppingList(): ActionStatus<ShoppingList>
    suspend fun setShoppingList(shoppingList: List<Purchase>): SimpleAction
    suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction
}
