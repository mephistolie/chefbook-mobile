package com.cactusknights.chefbook.domain.interfaces

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import kotlinx.coroutines.flow.StateFlow

interface ICategoriesCacheReader {
    suspend fun observeCategories(): StateFlow<List<Category>?>
    suspend fun getCategories(): List<Category>
}

interface ICategoriesCacheWriter {
    suspend fun emitCategories(categories: List<Category>)
    suspend fun addCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun removeCategory(categoryId: Int)
}

interface ICategoriesCache : ICategoriesCacheReader, ICategoriesCacheWriter

interface IRecipeBookCacheReader {
    suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?>
    suspend fun getRecipeBook(): List<RecipeInfo>
    suspend fun getRecipe(recipeId: Int): Recipe?
}

interface IRecipeBookCacheWriter {
    suspend fun emitRecipeBook(recipes: List<RecipeInfo>)
    suspend fun putRecipe(recipe: Recipe)
    suspend fun removeRecipe(recipeId: Int)

    suspend fun setRecipeSavedStatus(recipeId: Int, saved: Boolean)
    suspend fun setRecipeLikeStatus(recipeId: Int, liked: Boolean)
    suspend fun setRecipeFavouriteStatus(recipeId: Int, favourite: Boolean)
    suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>)

}

interface IRecipeBookCache : IRecipeBookCacheReader, IRecipeBookCacheWriter
