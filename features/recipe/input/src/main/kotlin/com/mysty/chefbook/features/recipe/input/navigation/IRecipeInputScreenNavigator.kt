package com.mysty.chefbook.features.recipe.input.navigation

import com.mysty.chefbook.navigation.navigators.IDialogNavigator

interface IRecipeInputScreenBaseNavigator : IDialogNavigator {
  fun openRecipeInputDetailsScreen()
  fun openSavedDialog()
  fun closeRecipeInput(recipeId: String? = null)
}

interface IRecipeInputDetailsScreenNavigator : IRecipeInputScreenBaseNavigator {
  fun openRecipeInputIngredientScreen()
  fun openVisibilityDialog()
  fun openLanguageDialog()
  fun openEncryptionStatePickerDialog()
  fun openEncryptedVaultScreen()
  fun openCaloriesDialog()
}

interface IRecipeInputIngredientsScreenNavigator : IRecipeInputScreenBaseNavigator {
  fun openRecipeInputCookingScreen()
  fun openIngredientDialog(ingredientId: String)
}

