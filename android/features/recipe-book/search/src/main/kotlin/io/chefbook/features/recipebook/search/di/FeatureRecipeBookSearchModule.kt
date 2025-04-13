package io.chefbook.features.recipebook.search.di

import io.chefbook.features.recipebook.search.ui.RecipeBookSearchScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureRecipeBookSearchModule() = module {
  viewModelOf(::RecipeBookSearchScreenViewModel)
}
