package io.chefbook.features.recipebook.dashboard.di

import io.chefbook.features.recipebook.dashboard.ui.DashboardScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeBookDashboardModule = module {
  viewModelOf(::DashboardScreenViewModel)
}
