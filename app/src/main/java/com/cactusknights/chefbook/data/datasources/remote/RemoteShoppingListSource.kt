package com.cactusknights.chefbook.data.datasources.remote

import com.cactusknights.chefbook.data.IShoppingListSource
import com.cactusknights.chefbook.data.dto.common.recipe.toEntity
import com.cactusknights.chefbook.data.dto.common.recipe.toSerializable
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.ShoppingListApi
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import javax.inject.Inject

class RemoteShoppingListSource @Inject constructor(
    private val api: ShoppingListApi,
    private val handleResponse: INetworkHandler,
) : IShoppingListSource {

    override suspend fun getShoppingList(): ActionStatus<ShoppingList> {
        val result = handleResponse { api.getShoppingList() }
        if (result.isFailure()) return result.toActionStatus().asFailure()
        return DataResult(result.body().toEntity())
    }

    override suspend fun setShoppingList(shoppingList: List<Purchase>): SimpleAction =
        handleResponse { api.setShoppingList(shoppingList.map(Purchase::toSerializable)) }.toActionStatus().asEmpty()

    override suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction =
        handleResponse { api.addToShoppingList(purchases.map(Purchase::toSerializable)) }.toActionStatus().asEmpty()

}
