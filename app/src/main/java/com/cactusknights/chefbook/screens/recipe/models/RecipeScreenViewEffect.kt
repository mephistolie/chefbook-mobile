package com.cactusknights.chefbook.screens.recipe.models

import com.cactusknights.chefbook.models.DecryptedRecipe

sealed class RecipeScreenViewEffect {
    class Message(val messageId : Int) : RecipeScreenViewEffect()
    class EditRecipe(val recipe: DecryptedRecipe) : RecipeScreenViewEffect()
    object RecipeDeleted : RecipeScreenViewEffect()
    object EnableAds : RecipeScreenViewEffect()
}