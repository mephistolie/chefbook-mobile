package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase

interface IAddToShoppingListUseCase {
    suspend operator fun invoke(purchase: Purchase): SimpleAction
    suspend operator fun invoke(purchases: List<Purchase>): SimpleAction
}

internal class AddToShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : IAddToShoppingListUseCase {

    override suspend operator fun invoke(purchase: Purchase) = invoke(listOf(purchase))
    override suspend operator fun invoke(purchases: List<Purchase>) = shoppingListRepo.addToShoppingList(purchases)

}
