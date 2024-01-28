package io.chefbook.sdk.core.api.internal.data.repositories

interface LocalDataRepository {

  suspend fun refreshData()

  suspend fun clearLocalData()
}
