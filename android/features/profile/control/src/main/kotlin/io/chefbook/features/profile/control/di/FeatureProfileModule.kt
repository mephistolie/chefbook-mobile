package io.chefbook.features.profile.control.di

import io.chefbook.features.profile.control.ui.ProfileScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureProfileModule() = module {
    viewModelOf(::ProfileScreenViewModel)
}
