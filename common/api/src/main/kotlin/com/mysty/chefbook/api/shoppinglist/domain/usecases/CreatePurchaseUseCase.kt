package com.mysty.chefbook.api.shoppinglist.domain.usecases

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo

interface ICreatePurchaseUseCase {
    suspend operator fun invoke(): ActionStatus<String>
}

internal class CreatePurchaseUseCase(
    private val shoppingListRepo: IShoppingListRepo,
) : ICreatePurchaseUseCase {

    override suspend operator fun invoke() = shoppingListRepo.createPurchase()

}
