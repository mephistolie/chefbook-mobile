package io.chefbook.features.recipebook.dashboard.di

import io.chefbook.features.recipebook.dashboard.ui.DashboardScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureRecipeBookDashboardModule() = module {
  viewModelOf(::DashboardScreenViewModel)
}
