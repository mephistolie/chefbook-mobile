package io.chefbook.features.profile.deletion.di

import io.chefbook.features.profile.deletion.ui.ProfileDeletionScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureProfileDeletionModule = module {
    viewModelOf(::ProfileDeletionScreenViewModel)
}
