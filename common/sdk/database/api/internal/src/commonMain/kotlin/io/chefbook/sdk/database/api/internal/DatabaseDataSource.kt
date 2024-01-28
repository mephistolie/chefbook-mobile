package io.chefbook.sdk.database.api.internal

import io.chefbook.libs.logger.Logger

abstract class DatabaseDataSource {

  protected suspend fun <T> safeQuery(block: suspend () -> Result<T>) =
    try {
      block()
    } catch (e: Exception) {
      Logger.e(e, "Exception during SQL query")
      Result.failure(e)
    }

  protected suspend fun <T> safeQueryResult(block: suspend () -> T): Result<T> =
    try {
      Result.success(block())
    } catch (e: Exception) {
      Logger.e(e, "Exception during SQL query")
      Result.failure(e)
    }
}
