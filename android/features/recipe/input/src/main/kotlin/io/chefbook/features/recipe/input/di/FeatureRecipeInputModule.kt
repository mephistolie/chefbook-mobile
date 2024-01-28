package io.chefbook.features.recipe.input.di

import io.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureRecipeInputModule = module {
  viewModel { (recipeId: String?) ->
    RecipeInputScreenViewModel(
      recipeId = recipeId,

      getRecipeUseCase = get(),
      createRecipeUseCase = get(),
      updateRecipeUseCase = get(),
      observeEncryptedVaultStateUseCase = get(),
      getDefaultRecipeLanguageUseCase = get(),
      setDefaultRecipeLanguageUseCase = get(),
      getEncryptedVaultStateUseCase = get(),
      deleteRecipeInputPictureUseCase = get(),

      dispatchers = get(),
      context = get(),
    )
  }
}
