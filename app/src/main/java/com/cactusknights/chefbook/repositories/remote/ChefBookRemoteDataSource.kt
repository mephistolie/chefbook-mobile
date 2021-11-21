package com.cactusknights.chefbook.repositories.remote

import com.cactusknights.chefbook.domain.RemoteDataSource
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.remote.datasources.*
import javax.inject.Inject

class ChefBookRemoteDataSource @Inject constructor(
    private val authSource: RemoteAuthDataSource,
    private val userSource: RemoteUserDataSource,
    private val recipesSource: RemoteRecipesDataSource,
    private val categoriesSource: RemoteCategoriesDataSource,
    private val shoppingListSource: RemoteShoppingListDataSource
) : RemoteDataSource {
    override suspend fun signUp(email: String, password: String) {
        return authSource.signUp(email, password)
    }

    override suspend fun signIn(email: String, password: String) {
        return authSource.signIn(email, password)
    }

    override suspend fun signOut() {
        return authSource.signOut()
    }

    override suspend fun getUserInfo(): User {
        return userSource.getUserInfo()
    }

    override suspend fun getRecipes(): ArrayList<Recipe> {
        return recipesSource.getRecipes()
    }

    override suspend fun addRecipe(recipe: Recipe) : Int {
        return recipesSource.addRecipe(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        return recipesSource.updateRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        return recipesSource.deleteRecipe(recipe)
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        return recipesSource.setRecipeFavouriteStatus(recipe)
    }

    override suspend fun getCategories(): ArrayList<Category> {
        return categoriesSource.getCategories()
    }

    override suspend fun addCategory(category: Category) : Int {
        return categoriesSource.addCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        return categoriesSource.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        return categoriesSource.deleteCategory(category)
    }

    override suspend fun getShoppingList(): ArrayList<Selectable<String>> {
        return shoppingListSource.getShoppingList()
    }

    override suspend fun setShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        return shoppingListSource.setShoppingList(shoppingList)
    }

    override suspend fun addToShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        return shoppingListSource.addToShoppingList(shoppingList)
    }
}