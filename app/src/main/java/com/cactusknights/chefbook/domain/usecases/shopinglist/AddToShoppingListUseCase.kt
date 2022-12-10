package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import javax.inject.Inject

interface IAddToShoppingListUseCase {
    suspend operator fun invoke(purchase: Purchase): SimpleAction
    suspend operator fun invoke(purchases: List<Purchase>): SimpleAction
}

class AddToShoppingListUseCase @Inject constructor(
    private val shoppingListRepo: IShoppingListRepo,
) : IAddToShoppingListUseCase {

    override suspend operator fun invoke(purchase: Purchase) = invoke(listOf(purchase))
    override suspend operator fun invoke(purchases: List<Purchase>) = shoppingListRepo.addToShoppingList(purchases)

}
