package com.mysty.chefbook.features.home.di

import com.mysty.chefbook.features.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureHomeModule = module {
    viewModelOf(::HomeViewModel)
}
