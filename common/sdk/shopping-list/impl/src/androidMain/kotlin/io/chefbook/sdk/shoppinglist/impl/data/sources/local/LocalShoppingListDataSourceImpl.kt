package io.chefbook.sdk.shoppinglist.impl.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.exceptions.notFoundResult
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListTypeSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.ShoppingListsSerializer
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto.toSerializable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class LocalShoppingListDataSourceImpl(
  context: Context,
) : LocalShoppingListDataSource {

  private val dataStore = DataStoreFactory.create(
    serializer = ShoppingListsSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

  override fun observeShoppingLists() = dataStore.data
    .map { shoppingLists -> shoppingLists.map { it.meta.toEntity() } }

  override suspend fun getShoppingLists() = Result.success(observeShoppingLists().first())

  override fun observeShoppingList(shoppingListId: String) =
    dataStore.data
      .map { shoppingLists -> shoppingLists.firstOrNull { it.meta.id == shoppingListId } }
      .map { it?.toEntity() }

  override suspend fun getShoppingList(shoppingListId: String): Result<ShoppingList> {
    val shoppingList =
      dataStore.data.first().firstOrNull { it.meta.id == shoppingListId }?.toEntity()

    return if (shoppingList != null) Result.success(shoppingList) else Result.failure(
      NotFoundException()
    )
  }

  override suspend fun getPersonalShoppingList(): Result<ShoppingList> {
    val shoppingList =
      dataStore.data.first().firstOrNull { it.meta.type == ShoppingListTypeSerializable.PERSONAL }
        ?.toEntity()

    return if (shoppingList != null) Result.success(shoppingList) else Result.failure(
      NotFoundException()
    )
  }

  override suspend fun setShoppingList(
    shoppingList: ShoppingList,
  ): Result<Int> {
    dataStore.updateData { shoppingLists ->
      val index = shoppingLists.indexOfFirst { it.meta.id == shoppingList.meta.id }
      return@updateData if (index > -1) {
        shoppingLists.map { if (it.meta.id == shoppingList.meta.id) shoppingList.toSerializable() else it }
      } else {
        shoppingLists.plus(shoppingList.toSerializable())
      }
    }

    return Result.success(shoppingList.version)
  }

  override suspend fun updateShoppingList(
    shoppingListId: String,
    update: (ShoppingList) -> ShoppingList
  ): EmptyResult {
    var changed = false

    dataStore.updateData { shoppingLists ->
      shoppingLists.map {
        if (it.meta.id == shoppingListId) {
          changed = true
          update(it.toEntity()).toSerializable()
        } else {
          it
        }
      }
    }

    return if (changed) successResult else notFoundResult()
  }

  override suspend fun setShoppingListVersion(shoppingListId: String, version: Int): EmptyResult {
    dataStore.updateData { shoppingLists ->
      shoppingLists.map {
        if (it.meta.id == shoppingListId) {
          it.copy(meta = it.meta.copy(version = version))
        } else {
          it
        }
      }
    }

    return successResult
  }


  override suspend fun addToShoppingList(
    shoppingListId: String,
    purchases: List<Purchase>,
    recipeNames: Map<String, String>,
    ): Result<Int> {
    var version: Int? = null
    dataStore.updateData { shoppingLists ->
      shoppingLists.map { shoppingList ->
        if (shoppingList.meta.id == shoppingListId) {
          shoppingList.toEntity().plusPurchases(purchases, recipeNames).toSerializable()
            .also { version = it.meta.version }
        } else {
          shoppingList
        }
      }
    }
    return version?.let { Result.success(it) } ?: Result.failure(NotFoundException())
  }

  override suspend fun deleteShoppingList(shoppingListId: String): EmptyResult {
    dataStore.updateData { shoppingLists ->
      shoppingLists.filter {
        it.meta.id != shoppingListId && it.meta.type != ShoppingListTypeSerializable.PERSONAL
      }
    }

    return successResult
  }

  override suspend fun clear() {
    dataStore.updateData { ShoppingListsSerializer.defaultValue }
  }

  companion object {
    private const val DATASTORE_FILE = "shopping_lists.json"
  }
}
