package io.chefbook.features.profile.deletion.di

import io.chefbook.features.profile.deletion.ui.ProfileDeletionScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureProfileDeletionModule() = module {
    viewModelOf(::ProfileDeletionScreenViewModel)
}
