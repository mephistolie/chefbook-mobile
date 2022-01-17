package com.cactusknights.chefbook.screens.recipe.models

import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Selectable
import java.io.Serializable

sealed class RecipeActivityViewEffect {
    class Message(val messageId : Int) : RecipeActivityViewEffect()
    class EditRecipe(val recipe: DecryptedRecipe) : RecipeActivityViewEffect()
    object RecipeDeleted : RecipeActivityViewEffect()
    object EnableAds : RecipeActivityViewEffect()
}