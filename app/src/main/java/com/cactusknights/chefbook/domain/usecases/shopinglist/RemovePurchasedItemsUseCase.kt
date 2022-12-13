package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import javax.inject.Inject

interface IRemovePurchasedItemsUseCase {
    suspend operator fun invoke(): SimpleAction
}

class RemovePurchasedItemsUseCase @Inject constructor(
    private val shoppingListRepo: IShoppingListRepo,
) : IRemovePurchasedItemsUseCase {

    override suspend operator fun invoke() = shoppingListRepo.removePurchasedItems()

}
