package io.chefbook.sdk.database.impl.di

import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.impl.drivers.DatabaseDriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sdkDatabaseModule = module {

  singleOf(::DatabaseDriverFactory)
  singleOf(::createDriver)
  singleOf(ChefBookDatabase::invoke)
}

private fun createDriver(factory: DatabaseDriverFactory) =
  factory.createDriver()
