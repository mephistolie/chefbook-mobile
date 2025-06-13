package io.chefbook.sdk.database.impl.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope

expect fun Scope.createDriver(): SqlDriver
