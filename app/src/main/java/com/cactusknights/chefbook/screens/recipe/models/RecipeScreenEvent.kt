package com.cactusknights.chefbook.screens.recipe.models

import android.content.Context
import com.cactusknights.chefbook.models.DecryptedRecipe

sealed class RecipeScreenEvent {
    class LoadRecipe(val id: Int?, val remoteId: Int?) : RecipeScreenEvent()
    object ChangeLikeStatus : RecipeScreenEvent()
    object AddRecipeToRecipeBook : RecipeScreenEvent()
    object ChangeFavouriteStatus : RecipeScreenEvent()
    class DecryptPicture(val encryptedPicture: ByteArray) : RecipeScreenEvent()
    object EditRecipe : RecipeScreenEvent()
    object DeleteRecipe : RecipeScreenEvent()
    class ChangeIngredientSelectStatus(val index: Int) : RecipeScreenEvent()
    class SetCategories(val categories: ArrayList<Int>) : RecipeScreenEvent()
    object AddSelectedToShoppingList :RecipeScreenEvent()
}