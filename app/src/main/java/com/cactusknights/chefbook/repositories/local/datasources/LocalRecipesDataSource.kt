package com.cactusknights.chefbook.repositories.local.datasources

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.SyncDataProto
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.repositories.RecipesDataSource
import com.cactusknights.chefbook.repositories.local.dao.RecipesDao
import com.cactusknights.chefbook.repositories.local.entities.toRecipe
import com.cactusknights.chefbook.repositories.local.entities.toRecipeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipesDataSource @Inject constructor(
    private val dao: RecipesDao,
    private val syncData: DataStore<SyncDataProto>,
) : RecipesDataSource {

    override suspend fun getRecipes(): ArrayList<Recipe> {
        val recipes = arrayListOf<Recipe>()
        dao.getRecipes().map { it.toRecipe() }.toCollection(recipes)
        recipes.map { recipe -> recipe.categories = dao.getRecipeCategories(recipe.id!!).map { it.id } as java.util.ArrayList<Int> }
        return recipes
    }

    override suspend fun addRecipe(recipe: Recipe): Recipe {
        val recipeId = dao.addRecipe(recipe.toRecipeEntity()).toInt()
        dao.setRecipeCategories(recipeId, recipe.categories)
        recipe.id = recipeId
        return recipe
    }

    override suspend fun getRecipe(recipeId: Int): Recipe {
        return dao.getRecipe(recipeId).toRecipe()
    }

    suspend fun getRecipeByRemoteId(remoteId: Int): Recipe? {
        return try {
            dao.getRecipeByRemoteId(remoteId).toRecipe()
        } catch (e: Exception) { null }
    }

    override suspend fun updateRecipe(recipe: Recipe) : Recipe {
        dao.updateRecipe(recipe.toRecipeEntity())
        return recipe
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        dao.deleteRecipe(recipe.id!!)
        if (recipe.remoteId != null) {
            syncData.updateData { it.deletedRecipesList.add(recipe.remoteId!!); it }
        }
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        dao.setRecipeFavourite(recipe.id!!, recipe.isFavourite)
    }

    suspend fun setRecipeLikeStatus(recipe: Recipe) {
        dao.setRecipeLikes(recipe.id!!, recipe.isLiked, recipe.likes)
    }

    suspend fun getDeletedRecipeRemoteIds(): List<Int> = syncData.data.take(1).first().deletedRecipesList

    override suspend fun setRecipeCategories(recipe: Recipe) {
        dao.deleteRecipeCategories(recipe.id!!)
        dao.addRecipeCategories(recipe.id!!, recipe.categories)
    }
}