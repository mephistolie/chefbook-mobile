package io.chefbook.features.community.recipes.di

import io.chefbook.features.community.recipes.ui.viewmodel.CommunityRecipesScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureCommunityRecipesModule() = module {
  viewModelOf(::CommunityRecipesScreenViewModel)
}
