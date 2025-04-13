package io.chefbook.features.community.languages.di

import io.chefbook.features.community.languages.ui.CommunityLanguagesScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureCommunityLanguagesModule() = module {
  viewModelOf(::CommunityLanguagesScreenViewModel)
}
