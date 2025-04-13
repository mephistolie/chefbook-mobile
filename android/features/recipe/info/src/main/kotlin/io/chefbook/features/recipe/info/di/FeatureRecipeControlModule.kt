package io.chefbook.features.recipe.info.di

import io.chefbook.features.recipe.info.ui.RecipeScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureRecipeModule() = module {
  viewModelOf(::RecipeScreenViewModel)
}
