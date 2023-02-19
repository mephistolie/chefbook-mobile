package com.mysty.chefbook.api.shoppinglist.data.remote

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import com.mysty.chefbook.api.shoppinglist.data.common.dto.toSerializable
import com.mysty.chefbook.api.shoppinglist.data.remote.api.ShoppingListApi
import com.mysty.chefbook.api.shoppinglist.data.repository.IShoppingListSource
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList

internal class RemoteShoppingListSource(
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
