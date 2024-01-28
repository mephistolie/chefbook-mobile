package io.chefbook.features.recipebook.search.di

import io.chefbook.features.recipebook.search.ui.RecipeBookSearchScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeBookSearchModule = module {
  viewModelOf(::RecipeBookSearchScreenViewModel)
}
