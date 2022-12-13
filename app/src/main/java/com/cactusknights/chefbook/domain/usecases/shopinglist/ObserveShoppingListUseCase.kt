package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import kotlinx.coroutines.flow.StateFlow

interface IObserveShoppingListUseCase {
    suspend operator fun invoke(): StateFlow<ShoppingList>
}

class ObserveShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : IObserveShoppingListUseCase {

    override suspend operator fun invoke() = shoppingListRepo.observeShoppingList()

}
