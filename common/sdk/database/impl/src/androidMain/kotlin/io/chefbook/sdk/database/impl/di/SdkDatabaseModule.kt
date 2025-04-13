package io.chefbook.sdk.database.impl.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.impl.ChefBookDataStoreFactoryImpl
import io.chefbook.sdk.database.impl.DATABASE_FILE
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkDatabaseModule() = module {

  singleOf(::createDriver)
  singleOf(ChefBookDatabase::invoke)

  singleOf(::ChefBookDataStoreFactoryImpl) bind ChefBookDataStoreFactory::class
}

private fun createDriver(context: Context) =
  AndroidSqliteDriver(ChefBookDatabase.Schema, context, DATABASE_FILE)
