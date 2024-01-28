package io.chefbook.sdk.database.impl.drivers

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.chefbook.sdk.database.api.internal.ChefBookDatabase

actual class DatabaseDriverFactory(private val context: Context) {

  actual fun createDriver(): SqlDriver =
    AndroidSqliteDriver(ChefBookDatabase.Schema, context, DATABASE_FILE)
}
