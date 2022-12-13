package com.cactusknights.chefbook.domain.usecases.recipe

import com.mysty.chefbook.core.coroutines.AppDispatchers
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.Successful
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.encrypt
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState
import com.cactusknights.chefbook.domain.entities.recipe.withoutPictures
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipePictureRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import javax.crypto.SecretKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.spongycastle.util.encoders.Base64

interface IUpdateRecipeUseCase {
    suspend operator fun invoke(recipeId: String, input: RecipeInput): Flow<ActionStatus<Recipe>>
}

class UpdateRecipeUseCase(
    private val recipeRepo: IRecipeRepo,
    private val pictureRepo: IRecipePictureRepo,
    private val vaultRepo: IEncryptedVaultRepo,
    private val encryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
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
                    val encryptedInput = inputWithoutPictures.encrypt { data -> encryptBytes(data, recipeKey) }
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
                if (isEncrypted && recipeKey != null) finalInput = finalInput.encrypt { data -> encryptBytes(data, recipeKey) }

                val result = recipeRepo.updateRecipe(recipeId, finalInput, recipeKey)
                if (result.isSuccess() && wasEncrypted && !isEncrypted) encryptionRepo.deleteRecipeKey(recipeId)
                emit(result)
            }
        }

    private fun encryptBytes(data: ByteArray, recipeKey: SecretKey) = Base64.toBase64String(cryptor.encryptDataBySymmetricKey(data, recipeKey))

}
