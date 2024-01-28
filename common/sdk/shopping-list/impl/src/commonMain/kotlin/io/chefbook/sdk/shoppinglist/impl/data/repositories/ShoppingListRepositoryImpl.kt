package io.chefbook.sdk.shoppinglist.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.exceptions.ServerException
import io.chefbook.libs.exceptions.notFoundResult
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.result.successResult
import io.chefbook.libs.utils.result.withCast
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.LocalShoppingListDataSource
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.PendingUploadsDataSource
import io.chefbook.sdk.shoppinglist.impl.data.sources.remote.RemoteShoppingListDataSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class ShoppingListRepositoryImpl(
  private val localSource: LocalShoppingListDataSource,
  private val remoteSource: RemoteShoppingListDataSource,

  private val pendingUploads: PendingUploadsDataSource,
  private val sources: DataSourcesRepository,
  private val scopes: CoroutineScopes,
  private val dispatchers: AppDispatchers,
) : ShoppingListRepository {

  private var launchSyncJob: Job? = launchSyncDataJob(COLD_STATE_SYNC_TIMEOUT)

  private var listenersCount = 0
  private val listenersMutex = Mutex()

  override fun observeShoppingLists() = localSource.observeShoppingLists()

  override suspend fun getShoppingLists() = Result.success(observeShoppingLists().first())

  override fun observeShoppingList(shoppingListId: String) =
    localSource.observeShoppingList(shoppingListId)
      .onStart {
        listenersMutex.withLock {
          if (listenersCount == 0) relaunchSyncDataJob(HOT_STATE_SYNC_TIMEOUT)
          listenersCount += 1
        }
      }
      .onCompletion {
        listenersMutex.withLock {
          listenersCount -= 1
          if (listenersCount == 0) relaunchSyncDataJob(COLD_STATE_SYNC_TIMEOUT)
        }
      }.flowOn(dispatchers.io)

  override suspend fun getShoppingList(shoppingListId: String?) =
    shoppingListId?.let { localSource.getShoppingList(shoppingListId) }
      ?: localSource.getPersonalShoppingList()

  override suspend fun createPurchase(shoppingListId: String): Result<String> {
    val purchase = Purchase(id = generateUUID(), name = "")
    return processPurchases(shoppingListId) { purchases -> purchases.plus(purchase) }.withCast { purchase.id }
  }

  override suspend fun updatePurchase(shoppingListId: String, purchase: Purchase) =
    processPurchases(shoppingListId) { purchases ->
      purchases.map { if (it.id == purchase.id) purchase else it }
    }

  override suspend fun switchPurchaseStatus(shoppingListId: String, purchaseId: String) =
    processPurchases(shoppingListId) { purchases ->
      purchases.map { if (it.id == purchaseId) it.copy(isPurchased = !it.isPurchased) else it }
    }

  override suspend fun removePurchasedItems(shoppingListId: String) =
    processPurchases(shoppingListId) { purchases -> purchases.filter { !it.isPurchased } }

  private suspend fun processPurchases(
    shoppingListId: String,
    process: (List<Purchase>) -> List<Purchase>,
  ) = withContext(dispatchers.io) {
    localSource.updateShoppingList(shoppingListId) { shoppingList ->
      shoppingList.copy(purchases = process(shoppingList.purchases))
    }
      .onSuccess { pendingUploads.markPendingUpload(shoppingListId) }
  }

  override suspend fun addToShoppingList(
    shoppingListId: String?,
    purchases: List<Purchase>,
    recipeNames: Map<String, String>,
  ): EmptyResult {
    val id = shoppingListId ?: getShoppingList().getOrNull()?.id ?: return notFoundResult()

    val localResult = localSource.addToShoppingList(id, purchases, recipeNames)
    if (!sources.isRemoteSourceAvailable()) {
      if (sources.isRemoteSourceEnabled()) pendingUploads.markPendingUpload(id)
      return localResult.asEmpty()
    }

    val remoteResult = remoteSource.addToShoppingList(id, purchases).onFailure { e ->
      if (e is ServerException && e.isVersionsConflict) {
        pendingUploads.markUploaded(id)
        pullShoppingList(id)
      }
    }

    return if (localResult.isSuccess || remoteResult.isSuccess) successResult else remoteResult.asEmpty()
  }

  private fun relaunchSyncDataJob(timeout: Long) {
    launchSyncJob?.cancel()
    launchSyncJob = launchSyncDataJob(timeout)
  }

  private fun launchSyncDataJob(timeout: Long) = scopes.repository.launch {
    while (isActive) {
      syncData()
      delay(timeout)
    }
  }

  private suspend fun syncData() {
    if (!sources.isRemoteSourceAvailable()) return

    remoteSource.getShoppingLists().onSuccess { remoteShoppingList ->
      commitShoppingListsChanges(localSource.getShoppingLists().getOrNull(), remoteShoppingList)
    }
  }

  private suspend fun commitShoppingListsChanges(
    localData: List<ShoppingListMeta>?,
    remoteData: List<ShoppingListMeta>,
  ) {
    val localIds = localData?.map(ShoppingListMeta::id) ?: emptyList()

    val pendingUploads = pendingUploads.getPendingUploads().toMutableSet()

    supervisorScope {
      localData?.forEach { localShoppingList ->
        val remoteShoppingList = remoteData.firstOrNull { it.id == localShoppingList.id }
        if (remoteShoppingList == null) {
          launch { localSource.deleteShoppingList(localShoppingList.id) }
          return@forEach
        }

        when {
          localShoppingList.id in pendingUploads && localShoppingList.version >= remoteShoppingList.version -> {
            launch { pushShoppingList(localShoppingList.id) }
          }

          localShoppingList.version < remoteShoppingList.version -> {
            launch { pullShoppingList(localShoppingList.id) }
          }
        }
      }

      remoteData.filter { it.id !in localIds }.forEach { remoteShoppingList ->
        launch { pullShoppingList(remoteShoppingList.id) }
      }
    }
  }

  private suspend fun pullShoppingList(shoppingListId: String) {
    remoteSource.getShoppingList(shoppingListId).onSuccess(localSource::setShoppingList)
  }

  private suspend fun pushShoppingList(shoppingListId: String) {
    val shoppingList = localSource.getShoppingList(shoppingListId).getOrNull() ?: return

    remoteSource.setShoppingList(shoppingList)
      .onSuccess { version ->
        localSource.setShoppingListVersion(shoppingListId, version)
        pendingUploads.markUploaded(shoppingListId)
      }
      .onFailure { e ->
        if (e is ServerException && e.isVersionsConflict) {
          pullShoppingList(shoppingListId)
          pendingUploads.markUploaded(shoppingListId)
        }
      }
  }

  override suspend fun refreshShoppingList() =
    relaunchSyncDataJob(COLD_STATE_SYNC_TIMEOUT)

  override suspend fun clearLocalData() {
    localSource.clear()
  }

  companion object {
    private const val COLD_STATE_SYNC_TIMEOUT = 5 * 60 * 1000L
    private const val HOT_STATE_SYNC_TIMEOUT = 8 * 1000L
  }
}
