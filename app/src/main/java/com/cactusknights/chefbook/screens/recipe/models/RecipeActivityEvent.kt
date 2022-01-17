package com.cactusknights.chefbook.screens.recipe.models

import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInputEvent
import java.io.Serializable

sealed class RecipeActivityEvent {
    class LoadRecipe(val recipe: DecryptedRecipe) : RecipeActivityEvent()
    class LoadRecipeByRemoteId(val remoteId: Int) : RecipeActivityEvent()
    object ChangeLikeStatus : RecipeActivityEvent()
    object AddRecipeToRecipeBook : RecipeActivityEvent()
    object ChangeFavouriteStatus : RecipeActivityEvent()
    object EditRecipe : RecipeActivityEvent()
    object DeleteRecipe : RecipeActivityEvent()
    class ChangeIngredientSelectStatus(val index: Int) : RecipeActivityEvent()
    class SetCategories(val categories: ArrayList<Int>) : RecipeActivityEvent()
    object AddSelectedToShoppingList :RecipeActivityEvent()
}