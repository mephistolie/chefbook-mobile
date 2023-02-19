package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList

interface IGetShoppingListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): ActionStatus<ShoppingList>
}

internal class GetShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : IGetShoppingListUseCase {

    override suspend operator fun invoke(forceRefresh: Boolean) = shoppingListRepo.getShoppingList(forceRefresh)

}
