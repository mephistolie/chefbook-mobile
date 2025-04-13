package io.chefbook.sdk.core.impl.di

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.di.scopes.ProfileScope
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.core.impl.data.repositories.DataSourcesRepositoryImpl
import io.chefbook.sdk.core.impl.data.repositories.LocalDataRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkCoreModule() = module {
  single { AppDispatchers() }
  singleOf(::CoroutineScopes)
  single<CoroutineScope>(named(ProfileScope.QUALIFIER)) {

  }

  singleOf(::DataSourcesRepositoryImpl) bind DataSourcesRepository::class
  singleOf(::LocalDataRepositoryImpl) bind LocalDataRepository::class


}
