package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase

interface IUpdatePurchaseUseCase {
    suspend operator fun invoke(purchase: Purchase) : SimpleAction
}

internal class UpdatePurchaseUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : IUpdatePurchaseUseCase {

    override suspend operator fun invoke(purchase: Purchase) = shoppingListRepo.updatePurchase(purchase)

}
