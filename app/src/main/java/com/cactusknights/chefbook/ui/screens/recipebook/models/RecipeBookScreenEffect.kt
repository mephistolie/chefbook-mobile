package com.cactusknights.chefbook.ui.screens.recipebook.models

sealed class RecipeBookScreenEffect {
    object RecipeSearchOpened : RecipeBookScreenEffect()
    object FavouriteOpened : RecipeBookScreenEffect()
    object CommunityRecipesOpened : RecipeBookScreenEffect()
    object EncryptionMenuOpened : RecipeBookScreenEffect()
    object RecipeCreationScreenOpened : RecipeBookScreenEffect()
    data class RecipeOpened(val recipeId: Int) : RecipeBookScreenEffect()
    data class CategoryOpened(val categoryId: Int) : RecipeBookScreenEffect()
}
