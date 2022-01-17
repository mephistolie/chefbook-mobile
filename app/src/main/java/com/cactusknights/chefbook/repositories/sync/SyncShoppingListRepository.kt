package com.cactusknights.chefbook.repositories.sync

import com.cactusknights.chefbook.domain.*
import com.cactusknights.chefbook.models.DataSource
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.repositories.local.datasources.LocalShoppingListDataSource
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteShoppingListDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class SyncShoppingListRepository @Inject constructor(
    private val localSource: LocalShoppingListDataSource,
    private val remoteSource: RemoteShoppingListDataSource,
    private val settings: SyncSettingsRepository
) : ShoppingListRepository {

    companion object {
        const val SYNC_TIMEOUT = 60000
        const val TIMESTAMP_DELTA = 10000
    }

    private val currentShoppingList: MutableStateFlow<ShoppingList> = MutableStateFlow(ShoppingList(arrayListOf()))

    private var syncTimestamp: Date? = null
    private val mutex = Mutex()

    override suspend fun listenToShoppingList(): StateFlow<ShoppingList> {
        mutex.withLock {
            if (syncTimestamp == null || (Date().time - syncTimestamp!!.time) > SYNC_TIMEOUT) getShoppingList()
            syncTimestamp = Date()
        }
        return currentShoppingList.asStateFlow()
    }

    override suspend fun syncShoppingList() {
        if (settings.getDataSourceType() == DataSource.REMOTE) {
            remoteSource.setShoppingList(currentShoppingList.value)
        }
    }

    override suspend fun getShoppingList(): ShoppingList {
        val localShoppingList = localSource.getShoppingList()
        currentShoppingList.emit(localShoppingList)
        return if (settings.getDataSourceType() == DataSource.REMOTE) {
            try {
                val remoteShoppingList = remoteSource.getShoppingList()
                syncShoppingList(localShoppingList, remoteShoppingList)
            } catch (e: Exception) {
                localShoppingList
            }
        } else { localShoppingList }
    }

    override suspend fun setShoppingList(shoppingList: ShoppingList) {
        localSource.setShoppingList(shoppingList)
        currentShoppingList.emit(localSource.getShoppingList())
    }

    override suspend fun addToShoppingList(purchases: List<Purchase>) {
        localSource.addToShoppingList(purchases)
        currentShoppingList.emit(localSource.getShoppingList())
    }

    private suspend fun syncShoppingList(localData: ShoppingList, remoteData: ShoppingList): ShoppingList {
        if (abs(localData.timestamp.time - remoteData.timestamp.time) < TIMESTAMP_DELTA) return localData
        return if (localData.timestamp.time > remoteData.timestamp.time) {
            remoteSource.setShoppingList(localData)
            localData
        } else {
            localSource.setShoppingList(remoteData)
            currentShoppingList.emit(remoteData)
            remoteData
        }
    }
}