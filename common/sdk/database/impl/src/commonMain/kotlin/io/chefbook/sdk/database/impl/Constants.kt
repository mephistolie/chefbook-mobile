package io.chefbook.sdk.database.impl

import app.cash.sqldelight.db.SqlDriver

internal const val DATABASE_FILE = "chefbook_database.db"

interface DatabaseDriverFactory {
  fun createDriver(): SqlDriver
}
