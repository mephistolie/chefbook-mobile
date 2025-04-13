package io.chefbook.features.settings.di

import io.chefbook.features.settings.ui.SettingsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureSettingsModule() = module {
    viewModelOf(::SettingsScreenViewModel)
}
