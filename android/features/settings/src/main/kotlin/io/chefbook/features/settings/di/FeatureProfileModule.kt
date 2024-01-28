package io.chefbook.features.settings.di

import io.chefbook.features.settings.ui.SettingsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureSettingsModule = module {
    viewModelOf(::SettingsScreenViewModel)
}
