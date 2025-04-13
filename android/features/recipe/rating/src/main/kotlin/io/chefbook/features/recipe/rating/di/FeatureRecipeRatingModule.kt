package io.chefbook.features.recipe.rating.di

import io.chefbook.features.recipe.rating.ui.RecipeRatingScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureRecipeRatingModule() = module {
  viewModelOf(::RecipeRatingScreenViewModel)
}
