package io.chefbook.features.profile.editing.di

import io.chefbook.features.profile.editing.ui.ProfileEditingScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureProfileEditingModule() = module {
    viewModelOf(::ProfileEditingScreenViewModel)
}
