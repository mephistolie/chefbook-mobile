package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo

interface ISyncShoppingListUseCase {
    suspend operator fun invoke()
}

class SyncShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : ISyncShoppingListUseCase {

    override suspend operator fun invoke() = shoppingListRepo.syncShoppingList()

}
