package io.chefbook.di

import coil.ImageLoader
import io.chefbook.core.android.qr.QRCodeWriter
import io.chefbook.sdk.network.impl.di.qualifiers.HttpClient
import io.chefbook.ui.delegates.IconSwitcherDelegate
import io.chefbook.ui.screens.main.AppViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
  singleOf(::QRCodeWriter)
  singleOf(::IconSwitcherDelegate)
  viewModel { AppViewModel(get(), get(), get()) }
}
