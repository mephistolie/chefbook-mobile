package com.mysty.chefbook.api.recipe.data.repositories

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.RecipesFilter


internal interface IRecipeSource {
    suspend fun getRecipeBook(): ActionStatus<List<RecipeInfo>>
    suspend fun getRecipe(recipeId: String): ActionStatus<Recipe>
    suspend fun deleteRecipe(recipeId: String): SimpleAction
}

internal interface ILocalRecipeSource : IRecipeSource {
    suspend fun createRecipe(recipe: Recipe): ActionStatus<String>
    suspend fun updateRecipe(recipe: Recipe): SimpleAction
}

internal interface IRemoteRecipeSource : IRecipeSource {
    suspend fun getRecipesByQuery(query: RecipesFilter): ActionStatus<List<RecipeInfo>>
    suspend fun createRecipe(input: RecipeInput): ActionStatus<String>
    suspend fun updateRecipe(recipeId: String, input: RecipeInput): SimpleAction
}

internal interface IRecipeInteractionSource {
    suspend fun setRecipeLikeStatus(recipeId: String, isLiked: Boolean): SimpleAction
    suspend fun setRecipeFavouriteStatus(recipeId: String, isFavourite: Boolean): SimpleAction
    suspend fun setRecipeCategories(recipeId: String, categories: List<String>): SimpleAction
}

internal interface IRemoteRecipeInteractionSource: IRecipeInteractionSource {
    suspend fun addRecipeToRecipeBook(recipeId: String): SimpleAction
    suspend fun removeFromRecipeToRecipeBook(recipeId: String): SimpleAction
}

internal interface ILocalRecipeInteractionSource : IRecipeInteractionSource {
    suspend fun setRecipeLikes(recipeId: String, likes: Int?): SimpleAction
}

internal interface IRecipePictureSource {
    suspend fun getPictures(recipeId: String): ActionStatus<List<String>>
    suspend fun addPicture(recipeId: String, data: ByteArray): ActionStatus<String>
    suspend fun deletePicture(recipeId: String, name: String): SimpleAction
}
