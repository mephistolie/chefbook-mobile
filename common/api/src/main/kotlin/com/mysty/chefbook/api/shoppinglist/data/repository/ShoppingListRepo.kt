package com.mysty.chefbook.api.shoppinglist.data.repository

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import java.time.ZoneOffset
import java.util.UUID
import kotlin.math.abs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ShoppingListRepo(
  private val localSource: IShoppingListSource,
  private val remoteSource: IShoppingListSource,

  private val source: ISourcesRepo,
  private val scopes: CoroutineScopes,
) : IShoppingListRepo {

  private val shoppingList: MutableStateFlow<ShoppingList> =
    MutableStateFlow(ShoppingList(purchases = emptyList()))

  private var refreshTimestamp: Long = 0

  override suspend fun observeShoppingList(): StateFlow<ShoppingList> {
    refreshData()
    return shoppingList.asStateFlow()
  }

  override suspend fun getShoppingList(forceRefresh: Boolean): ActionStatus<ShoppingList> {
    refreshData(forceRefresh)
    return DataResult(shoppingList.value)
  }

  override suspend fun refreshShoppingList() {
    refreshData(forceRefresh = true)
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

          if (remoteResult.data().timestamp > localResult.data().timestamp) {
            shoppingList.emit(remoteResult.data())
          }
          applyShoppingListChanges(localResult.data(), remoteResult.data())
        }

        refreshTimestamp = System.currentTimeMillis()
      }
    } else {
      val localResult = localSource.getShoppingList()
      if (localResult.isSuccess()) shoppingList.emit(localResult.data())
    }
  }

  private suspend fun applyShoppingListChanges(
    localData: ShoppingList,
    remoteData: ShoppingList
  ): ShoppingList {
    if (abs(
        localData.timestamp.toEpochSecond(ZoneOffset.UTC) - remoteData.timestamp.toEpochSecond(
          ZoneOffset.UTC
        )
      ) < TIMESTAMP_THRESHOLD
    ) return localData
    return if (localData.timestamp > remoteData.timestamp) {
      remoteSource.setShoppingList(localData.purchases.filter { it.name.isNotEmpty() })
      localData
    } else {
      localSource.setShoppingList(remoteData.purchases.filter { it.name.isNotEmpty() })
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

  override suspend fun createPurchase(): ActionStatus<String> {
    val purchase = Purchase(id = UUID.randomUUID().toString(), name = Strings.EMPTY)
    val result = processPurchases {
      val purchases = it.toMutableList()
      purchases.add(purchase)
      purchases.toList()
    }
    return if (result.isSuccess()) DataResult(purchase.id) else result.asFailure()
  }

  override suspend fun updatePurchase(purchase: Purchase) = processPurchases { purchases ->
    purchases.map { if (it.id == purchase.id) purchase else it }
  }

  override suspend fun switchPurchaseStatus(purchaseId: String) = processPurchases { purchases ->
    purchases.map { if (it.id == purchaseId) it.copy(isPurchased = !it.isPurchased) else it }
  }

  override suspend fun removePurchasedItems() =
    processPurchases { purchases -> purchases.filter { !it.isPurchased } }

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
