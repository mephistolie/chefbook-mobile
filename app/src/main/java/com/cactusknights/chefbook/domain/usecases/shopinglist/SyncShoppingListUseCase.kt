package com.cactusknights.chefbook.domain.usecases.shopinglist

import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import javax.inject.Inject

interface ISyncShoppingListUseCase {
    suspend operator fun invoke()
}

class SyncShoppingListUseCase @Inject constructor(
    private val shoppingListRepo: IShoppingListRepo,
) : ISyncShoppingListUseCase {

    override suspend operator fun invoke() = shoppingListRepo.syncShoppingList()

}
