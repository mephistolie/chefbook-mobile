package io.chefbook.sdk.database.impl.di

import app.cash.sqldelight.db.SqlDriver
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.impl.ChefBookDataStoreFactoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkDatabaseModule() = module {

  single { createDriver() }
  singleOf(ChefBookDatabase::invoke)

  singleOf(::ChefBookDataStoreFactoryImpl) bind ChefBookDataStoreFactory::class
}

expect fun Scope.createDriver(): SqlDriver
