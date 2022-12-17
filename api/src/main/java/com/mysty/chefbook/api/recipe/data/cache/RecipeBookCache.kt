package com.mysty.chefbook.api.recipe.data.cache

import com.mysty.chefbook.api.category.data.cache.ICategoriesCacheReader
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.toRecipeInfo
import com.mysty.chefbook.core.coroutines.AppDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

internal interface IRecipeBookCacheReader {
    suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?>
    suspend fun getRecipeBook(): List<RecipeInfo>
    suspend fun getRecipe(recipeId: String): Recipe?
}

internal interface IRecipeBookCacheWriter {
    suspend fun emitRecipeBook(recipes: List<RecipeInfo>)
    suspend fun putRecipe(recipe: Recipe)
    suspend fun removeRecipe(recipeId: String)

    suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean)
    suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean)
    suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean)
    suspend fun setRecipeCategories(recipeId: String, categories: List<String>)

}

internal interface IRecipeBookCache : IRecipeBookCacheReader, IRecipeBookCacheWriter

internal class RecipeBookCache(
    private val categoriesCache: ICategoriesCacheReader,
    private val dispatchers: AppDispatchers,
) : IRecipeBookCache {

    private val cachedRecipeBook = MutableStateFlow<List<RecipeInfo>?>(null)
    private val cachedRecipeMap = mutableMapOf<String, Recipe>()

    override suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?> = cachedRecipeBook.asStateFlow()

    override suspend fun getRecipeBook(): List<RecipeInfo> = cachedRecipeBook.value.orEmpty()

    override suspend fun getRecipe(recipeId: String): Recipe? {
        val recipe = cachedRecipeMap[recipeId]
        Timber.i("Requested recipe $recipeId is found: ${recipe != null}")
        return recipe
    }

    override suspend fun emitRecipeBook(recipes: List<RecipeInfo>) {
        cachedRecipeBook.emit(recipes)
        updateRecipeMap(recipes)
        Timber.i("${recipes.size} recipes were cached")
    }

    private fun updateRecipeMap(recipes: List<RecipeInfo>) = with(dispatchers.default) {
        val unchangedRecipes = mutableMapOf<String, Recipe>()
        recipes.forEach { recipeInfo ->
            val recipe = cachedRecipeMap[recipeInfo.id]
            if (recipe != null && recipeInfo.updateTimestamp <= recipe.updateTimestamp) {
                unchangedRecipes[recipe.id] = recipe
            }
        }
        cachedRecipeMap.clear()
        cachedRecipeMap.putAll(unchangedRecipes)
    }

    override suspend fun putRecipe(recipe: Recipe) {
        val updatedRecipes = cachedRecipeBook.value?.filter { it.id != recipe.id }?.toMutableList()
        updatedRecipes?.add(recipe.toRecipeInfo())
        cachedRecipeMap[recipe.id] = recipe
        cachedRecipeBook.emit(updatedRecipes)
        Timber.i("Recipe ${recipe.id} was put")
    }

    override suspend fun removeRecipe(recipeId: String) {
        cachedRecipeMap.remove(recipeId)
        cachedRecipeBook.emit(cachedRecipeBook.value?.filter { it.id != recipeId })
        Timber.i("Recipe $recipeId removed")
    }

    override suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean) {
        cachedRecipeMap[recipeId]?.copy(isSaved = saved)?.let { cachedRecipeMap[recipeId] = it }
        cachedRecipeBook.emit(cachedRecipeBook.value?.map { if (it.id != recipeId) it else it.copy(isSaved = saved) })
        Timber.i("Recipe $recipeId saved status changed to $saved")
    }

    override suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean) {
        cachedRecipeMap[recipeId]?.copy(isLiked = liked)?.let { cachedRecipeMap[recipeId] = it }
        cachedRecipeBook.emit(cachedRecipeBook.value?.map { if (it.id != recipeId) it else it.copy(isLiked = liked) })
        Timber.i("Recipe $recipeId like status changed to $liked")
    }

    override suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean) {
        cachedRecipeMap[recipeId]?.copy(isFavourite = favourite)?.let { cachedRecipeMap[recipeId] = it }
        cachedRecipeBook.emit(cachedRecipeBook.value?.map { if (it.id != recipeId) it else it.copy(isFavourite = favourite) })
        Timber.i("Recipe $recipeId like favourite changed to $favourite")
    }

    override suspend fun setRecipeCategories(recipeId: String, categories: List<String>) {
        val recipeCategories = categoriesCache.getCategories().filter { it.id in categories }
        cachedRecipeMap[recipeId]?.copy(categories = recipeCategories)?.let { cachedRecipeMap[recipeId] = it }
        cachedRecipeBook.emit(cachedRecipeBook.value?.map { if (it.id != recipeId) it else it.copy(categories = recipeCategories) })
        Timber.i("Recipe $recipeId categories $categories were set")
    }

}
