package io.chefbook.features.recipebook.category.di

import io.chefbook.features.recipebook.category.ui.CategoryRecipesScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureCategoryRecipesModule() = module {
  viewModelOf(::CategoryRecipesScreenViewModel)
}
