package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.crypto.ICryptor
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeEncryptionRepo
import com.mysty.chefbook.api.recipe.domain.IRecipePictureRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.withoutPictures
import com.mysty.chefbook.api.sources.domain.IRecipeCryptor
import com.mysty.chefbook.core.coroutines.AppDispatchers
import javax.crypto.SecretKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface IUpdateRecipeUseCase {
    suspend operator fun invoke(recipeId: String, input: RecipeInput): Flow<ActionStatus<Recipe>>
}

internal class UpdateRecipeUseCase(
    private val recipeRepo: IRecipeRepo,
    private val pictureRepo: IRecipePictureRepo,
    private val vaultRepo: IEncryptedVaultRepo,
    private val encryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
    private val recipeCryptor: IRecipeCryptor,
    private val dispatchers: AppDispatchers,
) : IUpdateRecipeUseCase {

    override suspend operator fun invoke(recipeId: String, input: RecipeInput): Flow<ActionStatus<Recipe>> =
        withContext(dispatchers.default) {
            flow {
                emit(Loading)
                val originalRecipe = recipeRepo.getRecipe(recipeId)
                val wasEncrypted = (originalRecipe as? Successful)?.data?.encryptionState !is EncryptionState.Standard
                val isEncrypted = input.isEncrypted

                var recipeKey: SecretKey? = null
                when {
                    wasEncrypted -> {
                        val userKeyResult = vaultRepo.getUserPrivateKey()
                        if (userKeyResult.isFailure()) {
                            emit(userKeyResult.asFailure())
                            return@flow
                        }
                        val recipeKeyResult = encryptionRepo.getRecipeKey(recipeId, userKeyResult.data())
                        if (recipeKeyResult.isFailure()) {
                            emit(recipeKeyResult.asFailure())
                            return@flow
                        }
                        recipeKey = recipeKeyResult.data()
                    }
                    isEncrypted -> {
                        val userKeyResult = vaultRepo.getUserPublicKey()
                        if (userKeyResult.isFailure()) {
                            emit(userKeyResult.asFailure())
                            return@flow
                        }
                        recipeKey = cryptor.generateSymmetricKey()
                        val recipeKeyResult = encryptionRepo.setRecipeKey(recipeId, recipeKey, userKeyResult.data())
                        if (recipeKeyResult.isFailure()) {
                            emit(userKeyResult.asFailure())
                            return@flow
                        }
                    }
                }

                if (isEncrypted && !wasEncrypted && recipeKey != null) {
                    val inputWithoutPictures = input.withoutPictures()
                    val encryptedInput = recipeCryptor.encryptRecipe(inputWithoutPictures, recipeKey)
                    val result = recipeRepo.updateRecipe(recipeId, encryptedInput, recipeKey)
                    if (result.isFailure()) {
                        emit(result.asFailure())
                        return@flow
                    }
                }

                var finalInput = pictureRepo.uploadRecipePictures(
                    recipeId = recipeId,
                    input = input,
                    key = recipeKey,
                    isEncrypted = isEncrypted,
                    wasEncrypted = wasEncrypted,
                )
                if (isEncrypted && recipeKey != null) finalInput = recipeCryptor.encryptRecipe(finalInput, recipeKey)

                val result = recipeRepo.updateRecipe(recipeId, finalInput, recipeKey)
                if (result.isSuccess() && wasEncrypted && !isEncrypted) encryptionRepo.deleteRecipeKey(recipeId)
                emit(result)
            }
        }

}
