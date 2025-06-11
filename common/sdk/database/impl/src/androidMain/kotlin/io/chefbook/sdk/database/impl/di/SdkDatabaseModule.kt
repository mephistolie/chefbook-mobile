package io.chefbook.sdk.database.impl.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.impl.DATABASE_FILE
import org.koin.core.scope.Scope

actual fun Scope.createDriver(): SqlDriver =
  AndroidSqliteDriver(ChefBookDatabase.Schema, context = get<Context>(), DATABASE_FILE)
