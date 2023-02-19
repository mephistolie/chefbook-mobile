package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo

interface IRemovePurchasedItemsUseCase {
    suspend operator fun invoke(): SimpleAction
}

internal class RemovePurchasedItemsUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : IRemovePurchasedItemsUseCase {

    override suspend operator fun invoke() = shoppingListRepo.removePurchasedItems()

}
