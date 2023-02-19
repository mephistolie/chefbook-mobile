package com.mysty.chefbook.features.auth.di

import com.mysty.chefbook.features.auth.ui.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureAuthModule = module {
    viewModelOf(::AuthViewModel)
}
