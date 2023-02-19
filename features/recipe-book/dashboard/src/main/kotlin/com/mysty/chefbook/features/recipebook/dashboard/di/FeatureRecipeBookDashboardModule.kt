package com.mysty.chefbook.features.recipebook.dashboard.di

import com.mysty.chefbook.features.recipebook.dashboard.ui.RecipeBookScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeBookDashboardModule = module {
    viewModelOf(::RecipeBookScreenViewModel)
}
