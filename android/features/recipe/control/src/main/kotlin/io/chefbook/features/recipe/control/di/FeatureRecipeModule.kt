package io.chefbook.features.recipe.control.di

import io.chefbook.features.recipe.control.ui.RecipeControlScreenViewModel
import io.chefbook.features.recipe.control.ui.components.categories.RecipeCategoriesSelectionBlockViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeControlModule = module {
  viewModelOf(::RecipeControlScreenViewModel)
  viewModelOf(::RecipeCategoriesSelectionBlockViewModel)
}
