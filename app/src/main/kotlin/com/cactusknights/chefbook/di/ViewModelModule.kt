package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.ui.screens.main.AppViewModel
import com.mysty.chefbook.core.android.qr.QRCodeWriter
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    singleOf(::QRCodeWriter)
    viewModel { AppViewModel(get(), get(), get(named(Qualifiers.ENCRYPTED_IMAGE)), get()) }
}
