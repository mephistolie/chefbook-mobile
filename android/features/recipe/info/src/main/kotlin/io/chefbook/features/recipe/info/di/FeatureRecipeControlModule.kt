package io.chefbook.features.recipe.info.di

import io.chefbook.features.recipe.info.ui.RecipeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeModule = module {
  viewModelOf(::RecipeScreenViewModel)
}
