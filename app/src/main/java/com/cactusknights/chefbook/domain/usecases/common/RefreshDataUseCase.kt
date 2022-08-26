package com.cactusknights.chefbook.domain.usecases.common

import com.cactusknights.chefbook.data.repositories.ShoppingListRepo
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import com.cactusknights.chefbook.domain.interfaces.IProfileRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface IRefreshDataUseCase {
    suspend operator fun invoke()
}

class RefreshDataUseCase @Inject constructor(
    private val profileRepo: IProfileRepo,
    private val recipeRepo: IRecipeRepo,
    private val categoriesRepo: ICategoryRepo,
    private val shoppingListRepo: ShoppingListRepo,
) : IRefreshDataUseCase {

    override suspend operator fun invoke() {
        CoroutineScope(Dispatchers.IO).launch {
            profileRepo.getProfile(forceRefresh = true)
            categoriesRepo.getCategories(forceRefresh = true)
            recipeRepo.getRecipeBook(forceRefresh = true)
            shoppingListRepo.getShoppingList(forceRefresh = true)
        }
    }

}
