package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList
import kotlinx.coroutines.flow.StateFlow

interface IObserveShoppingListUseCase {
    suspend operator fun invoke(): StateFlow<ShoppingList>
}

internal class ObserveShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : IObserveShoppingListUseCase {

    override suspend operator fun invoke() = shoppingListRepo.observeShoppingList()

}
