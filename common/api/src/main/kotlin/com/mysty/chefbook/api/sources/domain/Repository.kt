package com.mysty.chefbook.api.sources.domain

import kotlinx.coroutines.flow.StateFlow

internal interface ISourcesRepo {
    suspend fun observeServerAccess(): StateFlow<Boolean>
    suspend fun isOnlineMode(): Boolean
    suspend fun useRemoteSource(): Boolean
    suspend fun setServerAccess(hasConnection: Boolean)
    suspend fun clearLocalData()
}
