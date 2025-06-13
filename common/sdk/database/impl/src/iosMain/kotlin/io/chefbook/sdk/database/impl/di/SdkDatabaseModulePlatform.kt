package io.chefbook.sdk.database.impl.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.impl.DATABASE_FILE
import org.koin.core.scope.Scope

actual fun Scope.createDriver(): SqlDriver =
  NativeSqliteDriver(ChefBookDatabase.Schema, DATABASE_FILE)
