package io.chefbook.sdk.shoppinglist.impl.data.sources.local

import androidx.datastore.core.DataStore
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.exceptions.notFoundResult
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListTypeSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.ShoppingListsSerializer
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto.ShoppingListSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto.toSerializable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class LocalShoppingListDataSourceImpl(
  private val profileId: String,
  private val dataStore: DataStore<Map<String, List<ShoppingListSerializable>>>
) : LocalShoppingListDataSource {

  override fun observeShoppingLists() = dataStore.data
    .map { shoppingLists ->
      val profileShoppingLists = shoppingLists[profileId].orEmpty()
      profileShoppingLists.map { it.meta.toEntity() }
    }

  override suspend fun getShoppingLists() = Result.success(observeShoppingLists().first())

  override fun observeShoppingList(shoppingListId: String) = dataStore.data
      .map { shoppingLists ->
        val profileShoppingLists = shoppingLists[profileId].orEmpty()
        profileShoppingLists.firstOrNull { it.meta.id == shoppingListId }?.toEntity()
      }

  override suspend fun getShoppingList(shoppingListId: String): Result<ShoppingList> {
    val shoppingList = observeShoppingList(shoppingListId).first()
    return if (shoppingList != null) Result.success(shoppingList) else Result.failure(
      NotFoundException()
    )
  }

  override suspend fun getPersonalShoppingList(): Result<ShoppingList> {
    val shoppingLists = dataStore.data.first()
    val profileShoppingLists = shoppingLists[profileId].orEmpty()
    val shoppingList = profileShoppingLists
      .firstOrNull {it.meta.type == ShoppingListTypeSerializable.PERSONAL }?.toEntity()

    return if (shoppingList != null) Result.success(shoppingList) else Result.failure(
      NotFoundException()
    )
  }

  override suspend fun setShoppingList(
    shoppingList: ShoppingList,
  ): Result<Int> {
    dataStore.updateData { shoppingLists ->
      val profileShoppingLists = listOf(shoppingList.toSerializable())
        .plus(shoppingLists[profileId].orEmpty())
        .distinctBy { it.meta.id }
      shoppingLists.plus(profileId to profileShoppingLists)
    }

    return Result.success(shoppingList.version)
  }

  override suspend fun updateShoppingList(
    shoppingListId: String,
    update: (ShoppingList) -> ShoppingList
  ): EmptyResult {
    var changed = false
    dataStore.updateData { shoppingLists ->
      val profileShoppingLists = shoppingLists[profileId].orEmpty()
      val modifiedShoppingLists = profileShoppingLists.map {
        if (it.meta.id == shoppingListId) {
          changed = true
          update(it.toEntity()).toSerializable()
        } else {
          it
        }
      }
      shoppingLists.plus(profileId to modifiedShoppingLists)
    }

    return if (changed) successResult else notFoundResult()
  }

  override suspend fun setShoppingListVersion(shoppingListId: String, version: Int): EmptyResult {
    dataStore.updateData { shoppingLists ->
      val profileShoppingLists = shoppingLists[profileId].orEmpty()
      val modifiedShoppingLists = profileShoppingLists.map {
        if (it.meta.id == shoppingListId) {
          it.copy(meta = it.meta.copy(version = version))
        } else {
          it
        }
      }
      shoppingLists.plus(profileId to modifiedShoppingLists)
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
      val profileShoppingLists = shoppingLists[profileId].orEmpty()
      val modifiedShoppingLists = profileShoppingLists.map { shoppingList ->
        if (shoppingList.meta.id == shoppingListId) {
          shoppingList.toEntity().plusPurchases(purchases, recipeNames).toSerializable()
            .also { version = it.meta.version }
        } else {
          shoppingList
        }
      }
      shoppingLists.plus(profileId to modifiedShoppingLists)
    }
    return version?.let { Result.success(it) } ?: Result.failure(NotFoundException())
  }

  override suspend fun deleteShoppingList(shoppingListId: String): EmptyResult {
    dataStore.updateData { shoppingLists ->
      val profileShoppingLists = shoppingLists[profileId].orEmpty()
      val modifiedShoppingLists = profileShoppingLists.filter {
        it.meta.id != shoppingListId && it.meta.type != ShoppingListTypeSerializable.PERSONAL
      }
      shoppingLists.plus(profileId to modifiedShoppingLists)
    }

    return successResult
  }

  override suspend fun clear() {
    dataStore.updateData { ShoppingListsSerializer.defaultValue }
  }
}
