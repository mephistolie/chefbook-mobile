package com.mysty.chefbook.features.category.di

import com.mysty.chefbook.features.category.ui.input.CategoryInputDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureCategoryModule = module {
    viewModel { (categoryId: String?) -> CategoryInputDialogViewModel(categoryId = categoryId, get(), get(), get(), get()) }
}
