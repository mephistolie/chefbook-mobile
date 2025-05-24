package io.chefbook.features.category.di

import io.chefbook.features.category.ui.input.CategoryInputScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun featureCategoryModule() = module {
  viewModel { (categoryId: String?) ->
    CategoryInputScreenViewModel(
      collectionId = categoryId,
      get(),
      get(),
      get(),
      get()
    )
  }
}
