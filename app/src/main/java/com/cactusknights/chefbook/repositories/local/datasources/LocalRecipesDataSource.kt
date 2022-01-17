package com.cactusknights.chefbook.repositories.local.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cactusknights.chefbook.domain.RecipesDataSource
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.repositories.local.dao.RecipesDao
import com.cactusknights.chefbook.repositories.local.entities.toRecipe
import com.cactusknights.chefbook.repositories.local.entities.toRecipeEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import java.lang.reflect.Type
import javax.inject.Inject

class LocalRecipesDataSource @Inject constructor(
    private val dao: RecipesDao,
    private val gson: Gson,
    private val dataStore: DataStore<Preferences>,
) : RecipesDataSource {

    companion object {
        private val DELETED_RECIPE_REMOTE_IDS = stringPreferencesKey("deleted_recipes")
    }

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
            val deletedIds = getDeletedRecipeRemoteIds()
            deletedIds.add(recipe.remoteId!!)
            dataStore.edit { prefs ->
                prefs[DELETED_RECIPE_REMOTE_IDS] = gson.toJson(deletedIds)
            }
        }
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        dao.setRecipeFavourite(recipe.id!!, recipe.isFavourite)
    }

    suspend fun setRecipeLikeStatus(recipe: Recipe) {
        dao.setRecipeLikes(recipe.id!!, recipe.isLiked, recipe.likes)
    }

    suspend fun getDeletedRecipeRemoteIds(): ArrayList<Int> {
        val type: Type = object : TypeToken<ArrayList<Int>>() {}.type
        val prefs = dataStore.data.first()
        return gson.fromJson(prefs[DELETED_RECIPE_REMOTE_IDS]?: "[]", type)
    }

    override suspend fun setRecipeCategories(recipe: Recipe) {
        dao.deleteRecipeCategories(recipe.id!!)
        dao.addRecipeCategories(recipe.id!!, recipe.categories)
    }
}