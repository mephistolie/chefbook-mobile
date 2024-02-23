package io.chefbook.features.community.recipes.di

import io.chefbook.features.community.recipes.ui.viewmodel.CommunityRecipesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureCommunityRecipesModule = module {
  viewModelOf(::CommunityRecipesScreenViewModel)
}
