package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.crypto.ICryptor
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.recipe.domain.ILatestRecipesRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeEncryptionRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.sources.domain.IRecipeCryptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IGetRecipeUseCase {
    suspend operator fun invoke(recipeId: String): Flow<ActionStatus<Recipe>>
}

internal class GetRecipeUseCase(
    private val recipeRepo: IRecipeRepo,
    private val latestRecipesRepo: ILatestRecipesRepo,
    private val encryptedVaultRepo: IEncryptedVaultRepo,
    private val recipeEncryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
    private val recipeCryptor: IRecipeCryptor,
) : IGetRecipeUseCase {

    override suspend operator fun invoke(recipeId: String): Flow<ActionStatus<Recipe>> = flow {
        emit(Loading)

        val result = recipeRepo.getRecipe(recipeId)
        if (result.isFailure()) {
            emit(result)
            return@flow
        }

        latestRecipesRepo.pushRecipe(recipeId)
        var recipe = result.data()
        if (recipe.encryptionState is EncryptionState.Encrypted) {
            val userKey = encryptedVaultRepo.getUserPrivateKey()
            if (userKey.isFailure()) {
                emit(userKey.asFailure())
                return@flow
            }

            val recipeKey = recipeEncryptionRepo.getRecipeKey(recipeId, userKey.data())
            if (recipeKey.isFailure()) {
                emit(recipeKey.asFailure())
                return@flow
            }

            recipe = recipeCryptor.decryptRecipe(recipe, recipeKey.data())
        }
        recipeRepo.cacheRecipe(recipe)
        emit(DataResult(recipe))
    }

}
