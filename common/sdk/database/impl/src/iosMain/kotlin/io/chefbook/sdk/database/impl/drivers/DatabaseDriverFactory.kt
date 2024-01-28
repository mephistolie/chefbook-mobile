package io.chefbook.sdk.database.impl.drivers

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.chefbook.sdk.database.api.internal.ChefBookDatabase

actual class DatabaseDriverFactory {

  actual fun createDriver(): SqlDriver =
    NativeSqliteDriver(ChefBookDatabase.Schema, DATABASE_FILE)
}
