package io.chefbook.features.recipebook.favourite.di

import io.chefbook.features.recipebook.favourite.ui.FavouriteRecipesScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureFavouriteRecipesModule() = module {
  viewModelOf(::FavouriteRecipesScreenViewModel)
}
