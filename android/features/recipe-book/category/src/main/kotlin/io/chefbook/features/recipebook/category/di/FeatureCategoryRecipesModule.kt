package io.chefbook.features.recipebook.category.di

import io.chefbook.features.recipebook.category.ui.CategoryRecipesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureCategoryRecipesModule = module {
  viewModelOf(::CategoryRecipesScreenViewModel)
}
