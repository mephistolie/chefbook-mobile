package io.chefbook.features.profile.editing.di

import io.chefbook.features.profile.editing.ui.ProfileEditingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureProfileEditingModule = module {
    viewModelOf(::ProfileEditingScreenViewModel)
}
