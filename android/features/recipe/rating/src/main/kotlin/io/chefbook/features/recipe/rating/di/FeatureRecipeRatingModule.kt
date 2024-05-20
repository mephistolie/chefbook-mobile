package io.chefbook.features.recipe.rating.di

import io.chefbook.features.recipe.rating.ui.RecipeRatingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeRatingModule = module {
  viewModelOf(::RecipeRatingScreenViewModel)
}
