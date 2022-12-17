package com.mysty.chefbook.api.recipe.domain

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.RecipesFilter
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface IRecipeRepo {
    suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?>
    suspend fun getRecipeBook(forceRefresh: Boolean = false): List<RecipeInfo>
    suspend fun refreshRecipeBook()
    suspend fun getRecipesByQuery(query: RecipesFilter): ActionStatus<List<RecipeInfo>>
    suspend fun createRecipe(input: RecipeInput, key: SecretKey?): ActionStatus<Recipe>
    suspend fun getRecipe(recipeId: String): ActionStatus<Recipe>
    suspend fun cacheRecipe(recipe: Recipe): SimpleAction
    suspend fun updateRecipe(recipeId: String, input: RecipeInput, key: SecretKey?): ActionStatus<Recipe>
    suspend fun deleteRecipe(recipeId: String): SimpleAction
}

internal interface IRecipePictureRepo {
    suspend fun uploadRecipePictures(
        recipeId: String,
        input: RecipeInput,
        key: SecretKey?,
        isEncrypted: Boolean = key != null,
        wasEncrypted: Boolean = false
    ): RecipeInput
}

internal interface IRecipeInteractionRepo {
    suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean): SimpleAction
    suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean): SimpleAction
    suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean): SimpleAction
    suspend fun setRecipeCategories(recipeId: String, categories: List<String>): SimpleAction
}

internal interface IRecipeEncryptionRepo {
    suspend fun getRecipeKey(recipeId: String, userKey: PrivateKey): ActionStatus<SecretKey>
    suspend fun setRecipeKey(recipeId: String, recipeKey: SecretKey, userKey: PublicKey): SimpleAction
    suspend fun deleteRecipeKey(recipeId: String): SimpleAction
}

internal interface ILatestRecipesRepo {
    suspend fun observeLatestRecipes(): Flow<List<String>>
    suspend fun getLatestRecipes(): List<String>
    suspend fun pushRecipe(recipeId: String)
}
