package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo

interface ISwitchPurchaseStatusUseCase {
    suspend operator fun invoke(purchaseId: String): SimpleAction
}

internal class SwitchPurchaseStatusUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : ISwitchPurchaseStatusUseCase {

    override suspend operator fun invoke(purchaseId: String) = shoppingListRepo.switchPurchaseStatus(purchaseId)

}
