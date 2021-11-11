package com.cactusknights.chefbook.repositories.local

import com.cactusknights.chefbook.domain.RecipesProvider
import com.cactusknights.chefbook.models.BaseRecipe
import com.cactusknights.chefbook.repositories.local.dao.ChefBookDao
import com.cactusknights.chefbook.repositories.local.entities.toRecipe
import com.cactusknights.chefbook.repositories.local.entities.toRecipeEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    val dao: ChefBookDao
): RecipesProvider {
    override suspend fun getRecipes(): List<BaseRecipe> {
        return dao.getRecipes().map { it.toRecipe() }
    }

    override suspend fun addRecipe(recipe: BaseRecipe) {
        dao.addRecipe(recipe.toRecipeEntity())
    }

    override suspend fun updateRecipe(recipe: BaseRecipe) {
        dao.updateRecipe(recipe.toRecipeEntity())
    }

    override suspend fun deleteRecipe(recipeId: Int) {
        dao.deleteRecipe(recipeId)
    }

    override suspend fun setRecipeFavoriteStatus(recipe: BaseRecipe) {
        dao.setRecipeFavourite(recipe.id, recipe.isFavourite)
    }

    override suspend fun addToShoppingList(items: ArrayList<String>) {

    }


}