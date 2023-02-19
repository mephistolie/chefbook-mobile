package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo

interface ISyncShoppingListUseCase {
    suspend operator fun invoke()
}

internal class SyncShoppingListUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : ISyncShoppingListUseCase {

    override suspend operator fun invoke() = shoppingListRepo.syncShoppingList()

}
