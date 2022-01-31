package com.cactusknights.chefbook.repositories

import com.cactusknights.chefbook.models.*

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
    suspend fun setEncryptedUserKey(data: ByteArray)
    suspend fun deleteEncryptedUserKey()
    suspend fun getEncryptedRecipeKey(recipeId: Int): ByteArray?
    suspend fun setEncryptedRecipeKey(recipeId: Int, key: ByteArray)
    suspend fun deleteEncryptedRecipeKey(recipeId: Int)
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