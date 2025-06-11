package io.chefbook.di

import io.chefbook.core.android.qr.QRCodeWriter
import io.chefbook.ui.delegates.IconSwitcherDelegate
import io.chefbook.ui.screens.main.RootViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {
  singleOf(::QRCodeWriter)
  singleOf(::IconSwitcherDelegate)
  viewModel { RootViewModel(get(), get()) }
}
