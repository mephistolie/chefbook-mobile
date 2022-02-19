package com.cactusknights.chefbook.core.cache

import com.cactusknights.chefbook.models.RecipeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface RecipeBookCacheReader {
    suspend fun listenToRecipeBook(): StateFlow<List<RecipeInfo>?>
    suspend fun getRecipeBook() : List<RecipeInfo>
}

interface RecipeBookCacheWriter {
    suspend fun setRecipes(recipes: List<RecipeInfo>)
    suspend fun addRecipe(recipe: RecipeInfo)
    suspend fun updateRecipe(recipe: RecipeInfo)
    suspend fun deleteRecipe(recipe: RecipeInfo)
}

interface RecipeBookCacheManager : RecipeBookCacheReader, RecipeBookCacheWriter

class RecipeBookCacheManagerImpl : RecipeBookCacheManager {

    private val recipesCache: MutableStateFlow<List<RecipeInfo>?> = MutableStateFlow(null)

    override suspend fun listenToRecipeBook(): StateFlow<List<RecipeInfo>?> = recipesCache.asStateFlow()
    override suspend fun getRecipeBook(): List<RecipeInfo> = recipesCache.value?: emptyList()

    override suspend fun setRecipes(recipes: List<RecipeInfo>) = recipesCache.emit(recipes as ArrayList<RecipeInfo>)

    override suspend fun addRecipe(recipe: RecipeInfo) {
        val currentRecipes = recipesCache.value as ArrayList<RecipeInfo>
        currentRecipes.add(recipe)
        recipesCache.emit(currentRecipes)
    }

    override suspend fun updateRecipe(recipe: RecipeInfo) {
        val currentRecipes = recipesCache.value
        val updatedRecipes = currentRecipes?.map { if (sameId(it.id, recipe.id) || sameId(it.remoteId, recipe.remoteId)) recipe else it}
        recipesCache.emit(updatedRecipes)
    }

    override suspend fun deleteRecipe(recipe: RecipeInfo) {
        val currentRecipes = recipesCache.value
        val filteredRecipes = currentRecipes?.filter { !sameId(it.id, recipe.id) && !sameId(it.remoteId, recipe.remoteId) }
        recipesCache.emit(filteredRecipes)
    }

    private fun sameId(first: Int?, second: Int?) : Boolean = first == second && first != null
}