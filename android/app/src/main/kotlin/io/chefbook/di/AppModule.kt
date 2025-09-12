package io.chefbook.di

import io.chefbook.utils.android.qr.QRCodeWriter
import io.chefbook.ui.delegates.IconSwitcherDelegate
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun appModule() = module {
  singleOf(::QRCodeWriter)
  singleOf(::IconSwitcherDelegate)
}
