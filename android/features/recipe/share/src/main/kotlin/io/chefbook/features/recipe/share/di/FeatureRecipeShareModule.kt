package io.chefbook.features.recipe.share.di

import io.chefbook.features.recipe.share.ui.RecipeShareDialogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureRecipeShareModule() = module {
  viewModelOf(::RecipeShareDialogViewModel)
}