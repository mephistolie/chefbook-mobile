package io.chefbook.features.recipe.share.di

import io.chefbook.features.recipe.share.ui.RecipeShareDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeShareModule = module {
  viewModelOf(::RecipeShareDialogViewModel)
}