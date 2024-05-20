package io.chefbook.features.recipebook.creation.di

import io.chefbook.features.recipebook.creation.ui.RecipeBookCreationScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeBookCreationModule = module {
  viewModelOf(::RecipeBookCreationScreenViewModel)
}
