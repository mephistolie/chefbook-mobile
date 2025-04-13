package io.chefbook.features.recipebook.categories.di

import io.chefbook.features.recipebook.categories.ui.CategoriesScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureCategoriesModule() = module {
  viewModelOf(::CategoriesScreenViewModel)
}
