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
    suspend fun getRecipeKey(recipeId: String): ActionStatus<ByteArray>
    suspend fun setRecipeKey(recipeId: String, key: ByteArray): SimpleAction
    suspend fun deleteRecipeKey(recipeId: String): SimpleAction
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
    suspend fun getRecipe(recipeId: String): ActionStatus<Recipe>
    suspend fun deleteRecipe(recipeId: String): SimpleAction
}

interface ILocalRecipeSource : IRecipeSource {
    suspend fun createRecipe(recipe: Recipe): ActionStatus<String>
    suspend fun updateRecipe(recipe: Recipe): SimpleAction
}

interface IRemoteRecipeSource : IRecipeSource {
    suspend fun getRecipesByQuery(query: RecipesFilter): ActionStatus<List<RecipeInfo>>
    suspend fun createRecipe(input: RecipeInput): ActionStatus<String>
    suspend fun updateRecipe(recipeId: String, input: RecipeInput): SimpleAction
}

interface IRecipeInteractionSource {
    suspend fun setRecipeLikeStatus(recipeId: String, isLiked: Boolean): SimpleAction
    suspend fun setRecipeFavouriteStatus(recipeId: String, isFavourite: Boolean): SimpleAction
    suspend fun setRecipeCategories(recipeId: String, categories: List<String>): SimpleAction
}

interface IRemoteRecipeInteractionSource: IRecipeInteractionSource {
    suspend fun addRecipeToRecipeBook(recipeId: String): SimpleAction
    suspend fun removeFromRecipeToRecipeBook(recipeId: String): SimpleAction
}

interface ILocalRecipeInteractionSource : IRecipeInteractionSource {
    suspend fun setRecipeLikes(recipeId: String, likes: Int?): SimpleAction
}

interface IRecipePictureSource {
    suspend fun getPictures(recipeId: String): ActionStatus<List<String>>
    suspend fun addPicture(recipeId: String, data: ByteArray): ActionStatus<String>
    suspend fun deletePicture(recipeId: String, name: String): SimpleAction
}

interface ICategorySource {
    suspend fun getCategories(): ActionStatus<List<Category>>
    suspend fun getCategory(categoryId: String): ActionStatus<Category>
    suspend fun deleteCategory(categoryId: String): SimpleAction
}

interface ILocalCategorySource : ICategorySource {
    suspend fun createCategory(category: Category): ActionStatus<String>
    suspend fun updateCategory(category: Category): SimpleAction
}

interface IRemoteCategorySource : ICategorySource {
    suspend fun createCategory(input: CategoryInput): ActionStatus<String>
    suspend fun updateCategory(categoryId: String, input: CategoryInput): SimpleAction
}

interface IShoppingListSource {
    suspend fun getShoppingList(): ActionStatus<ShoppingList>
    suspend fun setShoppingList(shoppingList: List<Purchase>): SimpleAction
    suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction
}
