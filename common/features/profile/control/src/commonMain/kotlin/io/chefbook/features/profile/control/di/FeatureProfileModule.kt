package io.chefbook.features.profile.control.di

import io.chefbook.features.profile.control.mvi.ProfileStore
import io.chefbook.features.profile.control.mvi.ProfileStoreImpl
import io.chefbook.libs.di.scopes.ProfileComponent
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun featureProfileModule() = module {
  scope<ProfileComponent> {
    factoryOf(::ProfileStoreImpl) bind ProfileStore::class
  }
}
