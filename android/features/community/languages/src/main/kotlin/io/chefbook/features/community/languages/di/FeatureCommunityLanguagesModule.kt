package io.chefbook.features.community.languages.di

import io.chefbook.features.community.languages.ui.CommunityLanguagesScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureCommunityLanguagesModule = module {
  viewModelOf(::CommunityLanguagesScreenViewModel)
}
