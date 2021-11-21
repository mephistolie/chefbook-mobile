package com.cactusknights.chefbook.repositories.local

import com.cactusknights.chefbook.domain.LocalDataSource
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.local.dao.ChefBookDao
import com.cactusknights.chefbook.repositories.local.entities.toRecipe
import com.cactusknights.chefbook.repositories.local.entities.toRecipeEntity
import javax.inject.Inject

class ChefBookLocalDataSource @Inject constructor(
    val dao: ChefBookDao
): LocalDataSource {
    override suspend fun getUserInfo(): User {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipes(): ArrayList<Recipe> {
        val recipes = arrayListOf<Recipe>()
        return dao.getRecipes().map { it.toRecipe() }.toCollection(recipes)
    }

    override suspend fun addRecipe(recipe: Recipe) : Int {
        dao.addRecipe(recipe.toRecipeEntity())
        return 0
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        dao.updateRecipe(recipe.toRecipeEntity())
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        dao.deleteRecipe(recipe.id)
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        dao.setRecipeFavourite(recipe.id, recipe.isFavourite)
    }

    override suspend fun getShoppingList(): ArrayList<Selectable<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun setShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        TODO("Not yet implemented")
    }

    override suspend fun addToShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        TODO("Not yet implemented")
    }
}