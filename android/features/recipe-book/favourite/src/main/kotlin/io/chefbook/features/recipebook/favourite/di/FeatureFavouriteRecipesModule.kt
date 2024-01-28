package io.chefbook.features.recipebook.favourite.di

import io.chefbook.features.recipebook.favourite.ui.FavouriteRecipesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureFavouriteRecipesModule = module {
  viewModelOf(::FavouriteRecipesScreenViewModel)
}
