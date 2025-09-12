package io.chefbook.features.root.di

import io.chefbook.features.root.ui.mvi.RootStore
import io.chefbook.features.root.ui.mvi.RootStoreImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun featureRootModule() = module {
  factoryOf(::RootStoreImpl) bind RootStore::class
}
