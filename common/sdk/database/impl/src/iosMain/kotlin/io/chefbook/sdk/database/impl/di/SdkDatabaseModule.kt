package io.chefbook.sdk.database.impl.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.impl.DATABASE_FILE
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun sdkDatabaseModule() = module {

  singleOf(::createDriver)
  singleOf(ChefBookDatabase::invoke)
}

private fun createDriver() =
  NativeSqliteDriver(ChefBookDatabase.Schema, DATABASE_FILE)
