package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase

interface ISetShoppingListUseCase {
    suspend operator fun invoke(purchases: List<Purchase>): SimpleAction
}

internal class SetShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : ISetShoppingListUseCase {

    override suspend operator fun invoke(purchases: List<Purchase>) = shoppingListRepo.setShoppingList(purchases)

}
