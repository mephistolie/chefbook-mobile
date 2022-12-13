package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.data.IShoppingListSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import com.cactusknights.chefbook.domain.interfaces.ISourceRepo
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import java.time.ZoneOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

class ShoppingListRepo(
    private val localSource: IShoppingListSource,
    private val remoteSource: IShoppingListSource,

    private val source: ISourceRepo,
    private val scopes: CoroutineScopes,
) : IShoppingListRepo {

    private val shoppingList: MutableStateFlow<ShoppingList> = MutableStateFlow(ShoppingList(purchases = emptyList()))

    private var refreshTimestamp: Long = 0

    override suspend fun observeShoppingList(): StateFlow<ShoppingList> {
        refreshData()
        return shoppingList.asStateFlow()
    }

    override suspend fun getShoppingList(forceRefresh: Boolean): ActionStatus<ShoppingList> {
        refreshData(forceRefresh)
        return DataResult(shoppingList.value)
    }

    override suspend fun syncShoppingList() {
        scopes.repository.launch { refreshData(forceRefresh = true) }
    }

    private suspend fun refreshData(forceRefresh: Boolean = false) {
        if (source.useRemoteSource()) {
            if (forceRefresh || abs(System.currentTimeMillis() - refreshTimestamp) > REFRESH_TIME_THRESHOLD) {

                val localResult = localSource.getShoppingList()
                if (localResult.isFailure()) return
                shoppingList.emit(localResult.data())

                val remoteResult = remoteSource.getShoppingList()
                if (remoteResult.isSuccess()) {

                    if (remoteResult.data().timestamp > localResult.data().timestamp) shoppingList.emit(remoteResult.data())
                    applyShoppingListChanges(localResult.data(), remoteResult.data())
                }

                refreshTimestamp = System.currentTimeMillis()
            }
        } else {
            val localResult = localSource.getShoppingList()
            if (localResult.isSuccess()) shoppingList.emit(localResult.data())
        }
    }

    private suspend fun applyShoppingListChanges(localData: ShoppingList, remoteData: ShoppingList): ShoppingList {
        if (abs(localData.timestamp.toEpochSecond(ZoneOffset.UTC) - remoteData.timestamp.toEpochSecond(ZoneOffset.UTC)) < TIMESTAMP_THRESHOLD) return localData
        Timber.e("Test ${localData.timestamp} ${remoteData.timestamp}")
        return if (localData.timestamp > remoteData.timestamp) {
            remoteSource.setShoppingList(localData.purchases)
            localData
        } else {
            localSource.setShoppingList(remoteData.purchases)
            shoppingList.emit(remoteData)
            remoteData
        }
    }

    override suspend fun setShoppingList(purchases: List<Purchase>): SimpleAction {
        val result = localSource.setShoppingList(purchases)
        if (source.useRemoteSource()) {
            scopes.repository.launch { remoteSource.setShoppingList(purchases) }
        }

        if (result.isSuccess()) shoppingList.emit(localSource.getShoppingList().data())
        return result
    }

    override suspend fun switchPurchaseStatus(purchaseId: String) = processPurchases { purchases ->
        purchases.map { if (it.id == purchaseId) it.copy(isPurchased = !it.isPurchased) else it }
    }

    override suspend fun removePurchasedItems() = processPurchases { purchases -> purchases.filter { !it.isPurchased } }

    private suspend fun processPurchases(process: (List<Purchase>) -> List<Purchase>): SimpleAction {
        val currentShoppingList = (getShoppingList() as? ActionStatus.Success)?.data ?: return Failure()
        val purchases = process(currentShoppingList.purchases)
        shoppingList.emit(ShoppingList(purchases))
        localSource.setShoppingList(purchases)
        return SuccessResult
    }

    override suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction {
        val result = localSource.addToShoppingList(purchases)
        if (source.useRemoteSource()) {
            scopes.repository.launch { remoteSource.addToShoppingList(purchases) }
        }

        if (result.isSuccess()) shoppingList.emit(localSource.getShoppingList().data())
        return result
    }

    companion object {
        private const val REFRESH_TIME_THRESHOLD = 5 * 60 * 1000
        private const val TIMESTAMP_THRESHOLD = 10
    }

}
