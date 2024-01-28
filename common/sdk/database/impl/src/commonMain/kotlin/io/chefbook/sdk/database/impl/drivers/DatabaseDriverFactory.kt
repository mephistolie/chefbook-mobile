package io.chefbook.sdk.database.impl.drivers

import app.cash.sqldelight.db.SqlDriver

internal const val DATABASE_FILE = "chefbook_database.db"

expect class DatabaseDriverFactory {
  fun createDriver(): SqlDriver
}
