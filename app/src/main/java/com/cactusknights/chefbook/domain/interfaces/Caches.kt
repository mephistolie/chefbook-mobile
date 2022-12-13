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
    suspend fun removeCategory(categoryId: String)
}

interface ICategoriesCache : ICategoriesCacheReader, ICategoriesCacheWriter

interface IRecipeBookCacheReader {
    suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?>
    suspend fun getRecipeBook(): List<RecipeInfo>
    suspend fun getRecipe(recipeId: String): Recipe?
}

interface IRecipeBookCacheWriter {
    suspend fun emitRecipeBook(recipes: List<RecipeInfo>)
    suspend fun putRecipe(recipe: Recipe)
    suspend fun removeRecipe(recipeId: String)

    suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean)
    suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean)
    suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean)
    suspend fun setRecipeCategories(recipeId: String, categories: List<String>)

}

interface IRecipeBookCache : IRecipeBookCacheReader, IRecipeBookCacheWriter
