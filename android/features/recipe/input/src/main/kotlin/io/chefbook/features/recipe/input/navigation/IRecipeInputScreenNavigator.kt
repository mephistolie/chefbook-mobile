package io.chefbook.features.recipe.input.navigation

import io.chefbook.navigation.navigators.DialogNavigator

interface RecipeInputScreenBaseNavigator : DialogNavigator {
  fun openRecipeInputDetailsScreen()
  fun openSavedDialog()
  fun closeRecipeInput(recipeId: String? = null)
}

interface RecipeInputDetailsScreenNavigator :
  io.chefbook.features.recipe.input.navigation.RecipeInputScreenBaseNavigator {
  fun openRecipeInputIngredientScreen()
  fun openVisibilityDialog()
  fun openLanguageDialog()
  fun openEncryptionStatePickerDialog()
  fun openEncryptedVaultScreen()
  fun openCaloriesDialog()
}

interface RecipeInputIngredientsScreenNavigator :
  io.chefbook.features.recipe.input.navigation.RecipeInputScreenBaseNavigator {
  fun openRecipeInputCookingScreen()
  fun openIngredientDialog(ingredientId: String)
}

