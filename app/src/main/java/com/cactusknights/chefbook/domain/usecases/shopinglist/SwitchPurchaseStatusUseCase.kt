package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import javax.inject.Inject

interface ISwitchPurchaseStatusUseCase {
    suspend operator fun invoke(purchaseId: String): SimpleAction
}

class SwitchPurchaseStatusUseCase @Inject constructor(
    private val shoppingListRepo: IShoppingListRepo,
) : ISwitchPurchaseStatusUseCase {

    override suspend operator fun invoke(purchaseId: String) = shoppingListRepo.switchPurchaseStatus(purchaseId)

}
