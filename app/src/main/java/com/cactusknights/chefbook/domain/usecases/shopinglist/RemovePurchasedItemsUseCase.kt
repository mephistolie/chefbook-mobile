package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo

interface IRemovePurchasedItemsUseCase {
    suspend operator fun invoke(): SimpleAction
}

class RemovePurchasedItemsUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : IRemovePurchasedItemsUseCase {

    override suspend operator fun invoke() = shoppingListRepo.removePurchasedItems()

}
