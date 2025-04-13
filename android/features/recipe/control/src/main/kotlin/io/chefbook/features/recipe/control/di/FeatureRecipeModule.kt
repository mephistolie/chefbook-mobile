package io.chefbook.features.recipe.control.di

import io.chefbook.features.recipe.control.ui.RecipeControlScreenViewModel
import io.chefbook.features.recipe.control.ui.components.categories.RecipeCategoriesSelectionBlockViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureRecipeControlModule() = module {
  viewModelOf(::RecipeControlScreenViewModel)
  viewModelOf(::RecipeCategoriesSelectionBlockViewModel)
}
