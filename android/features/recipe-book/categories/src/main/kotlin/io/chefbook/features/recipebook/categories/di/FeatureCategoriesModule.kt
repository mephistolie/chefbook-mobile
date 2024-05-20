package io.chefbook.features.recipebook.categories.di

import io.chefbook.features.recipebook.categories.ui.CategoriesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureCategoriesModule = module {
  viewModelOf(::CategoriesScreenViewModel)
}
