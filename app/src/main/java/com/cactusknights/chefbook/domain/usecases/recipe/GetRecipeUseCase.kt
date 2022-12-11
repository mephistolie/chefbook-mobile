package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.decrypt
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import javax.crypto.SecretKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.spongycastle.util.encoders.Base64

interface IGetRecipeUseCase {
    suspend operator fun invoke(recipeId: Int): Flow<ActionStatus<Recipe>>
}

class GetRecipeUseCase @Inject constructor(
    private val recipeRepo: IRecipeRepo,
    private val latestRecipesRepo: ILatestRecipesRepo,
    private val encryptedVaultRepo: IEncryptedVaultRepo,
    private val recipeEncryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
) : IGetRecipeUseCase {

    override suspend operator fun invoke(recipeId: Int): Flow<ActionStatus<Recipe>> = flow {
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

            recipe = recipe.decrypt(recipeKey.data()) { data -> decryptBytes(data, recipeKey.data()) }
        }
        recipeRepo.cacheRecipe(recipe)
        emit(DataResult(recipe))
    }

    private fun decryptBytes(data: String, recipeKey: SecretKey) =
        cryptor.decryptDataBySymmetricKey(Base64.decode(data), recipeKey)

}
