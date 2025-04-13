package io.chefbook.features.recipebook.creation.di

import io.chefbook.features.recipebook.creation.ui.RecipeBookCreationScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureRecipeBookCreationModule() = module {
  viewModelOf(::RecipeBookCreationScreenViewModel)
}
