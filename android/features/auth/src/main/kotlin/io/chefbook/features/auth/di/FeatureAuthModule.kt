package io.chefbook.features.auth.di

import io.chefbook.features.auth.ui.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureAuthModule = module {
    viewModelOf(::AuthViewModel)
}
