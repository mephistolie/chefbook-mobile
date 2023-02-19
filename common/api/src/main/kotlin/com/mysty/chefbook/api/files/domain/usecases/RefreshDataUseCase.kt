package com.mysty.chefbook.api.files.domain.usecases

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.profile.domain.IProfileRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.shoppinglist.domain.IShoppingListRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface IRefreshDataUseCase {
    suspend operator fun invoke()
}

internal class RefreshDataUseCase(
    private val profileRepo: IProfileRepo,
    private val encryptionRepo: IEncryptedVaultRepo,
    private val recipeRepo: IRecipeRepo,
    private val categoriesRepo: ICategoryRepo,
    private val shoppingListRepo: IShoppingListRepo,
) : IRefreshDataUseCase {

    override suspend operator fun invoke() {
        CoroutineScope(Dispatchers.IO).launch {
            profileRepo.refreshProfile()
            encryptionRepo.refreshEncryptedVaultState()
            categoriesRepo.refreshCategories()
            recipeRepo.refreshRecipeBook()
            shoppingListRepo.refreshShoppingList()
        }
    }

}
