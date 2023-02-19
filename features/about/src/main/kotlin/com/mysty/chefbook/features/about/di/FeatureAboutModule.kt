package com.mysty.chefbook.features.about.di

import com.mysty.chefbook.features.about.ui.AboutScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureAboutModule = module {
    viewModelOf(::AboutScreenViewModel)
}
