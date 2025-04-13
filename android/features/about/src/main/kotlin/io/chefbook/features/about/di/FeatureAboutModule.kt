package io.chefbook.features.about.di

import io.chefbook.features.about.ui.AboutScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureAboutModule() = module {
  viewModelOf(::AboutScreenViewModel)
}
