package com.cactusknights.chefbook.domain

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthDataSource {
    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
}

interface UserDataSource {
    suspend fun getUserInfo(): User
}

interface RecipesDataSource {
    suspend fun getRecipes(): ArrayList<Recipe>
    suspend fun addRecipe(recipe: Recipe) : Int
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun setRecipeFavouriteStatus(recipe: Recipe)
}

interface CategoriesDataSource {
    suspend fun getCategories(): ArrayList<Category>
    suspend fun addCategory(category: Category) : Int
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}

interface ShoppingListDataSource {
    suspend fun getShoppingList(): ArrayList<Selectable<String>>
    suspend fun setShoppingList(shoppingList: ArrayList<Selectable<String>>)
    suspend fun addToShoppingList(shoppingList: ArrayList<Selectable<String>>)
}

interface LocalDataSource: UserDataSource, RecipesDataSource, ShoppingListDataSource
interface RemoteDataSource: AuthDataSource, UserDataSource, RecipesDataSource, CategoriesDataSource, ShoppingListDataSource

interface AuthRepository: AuthDataSource

interface UserRepository: AuthDataSource {
    suspend fun listenToUser(): MutableStateFlow<User?>
}

interface RecipesRepository: RecipesDataSource {
    suspend fun listenToUserRecipes(): MutableStateFlow<ArrayList<Recipe>>
}

interface CategoriesRepository: CategoriesDataSource

interface ShoppingListRepository: ShoppingListDataSource

interface ContentRepository: AuthRepository, UserRepository, RecipesRepository, CategoriesRepository, ShoppingListRepository