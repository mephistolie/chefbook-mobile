package com.cactusknights.chefbook.data.cache

import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCacheReader
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCache
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class RecipeBookCache @Inject constructor(
    private val categoriesCache: ICategoriesCacheReader,
) : IRecipeBookCache {

    private val cachedList = MutableStateFlow<List<RecipeInfo>?>(null)

    override suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?> = cachedList.asStateFlow()

    override suspend fun getRecipeBook(): List<RecipeInfo> = cachedList.value.orEmpty()

    override suspend fun emitRecipeBook(recipes: List<RecipeInfo>) {
        cachedList.emit(recipes)
    }

    override suspend fun addRecipe(recipe: RecipeInfo) {
        val updatedRecipes = cachedList.value?.filter { it.id != recipe.id }?.toMutableList()
        updatedRecipes?.add(recipe)
        cachedList.emit(updatedRecipes)
    }

    override suspend fun updateRecipe(recipe: RecipeInfo) {
        cachedList.emit(cachedList.value?.map { if (it.id != recipe.id) it else recipe })
    }

    override suspend fun removeRecipe(recipeId: Int) {
        cachedList.emit(cachedList.value?.filter { it.id != recipeId })
    }

    override suspend fun setRecipeSavedStatus(recipeId: Int, saved: Boolean) {
        cachedList.emit(cachedList.value?.map { if (it.id != recipeId) it else it.copy(isSaved = saved) })
    }

    override suspend fun setRecipeLikeStatus(recipeId: Int, liked: Boolean) {
        cachedList.emit(cachedList.value?.map { if (it.id != recipeId) it else it.copy(isLiked = liked) })
    }

    override suspend fun setRecipeFavouriteStatus(recipeId: Int, favourite: Boolean) {
        cachedList.emit(cachedList.value?.map { if (it.id != recipeId) it else it.copy(isFavourite = favourite) })
    }

    override suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>) {
        val recipeCategories = categoriesCache.getCategories().filter { it.id in categories }
        cachedList.emit(cachedList.value?.map { if (it.id != recipeId) it else it.copy(categories = recipeCategories) })
    }

}
