package io.chefbook.features.profile.control.di

import io.chefbook.features.profile.control.ui.ProfileScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureProfileModule = module {
    viewModelOf(::ProfileScreenViewModel)
}
