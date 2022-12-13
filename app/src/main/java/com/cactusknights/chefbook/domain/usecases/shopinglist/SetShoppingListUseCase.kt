package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo

interface ISetShoppingListUseCase {
    suspend operator fun invoke(purchases: List<Purchase>): SimpleAction
}

class SetShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : ISetShoppingListUseCase {

    override suspend operator fun invoke(purchases: List<Purchase>) = shoppingListRepo.setShoppingList(purchases)

}
