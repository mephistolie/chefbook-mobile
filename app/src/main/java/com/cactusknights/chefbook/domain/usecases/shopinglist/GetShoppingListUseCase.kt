package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import javax.inject.Inject

interface IGetShoppingListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): ActionStatus<ShoppingList>
}

class GetShoppingListUseCase @Inject constructor(
    private val shoppingListRepo: IShoppingListRepo,
) : IGetShoppingListUseCase {

    override suspend operator fun invoke(forceRefresh: Boolean) = shoppingListRepo.getShoppingList(forceRefresh)

}
