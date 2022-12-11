package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.core.coroutines.AppDispatchers
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.encrypt
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

interface ICreateRecipeUseCase {
    suspend operator fun invoke(input: RecipeInput): Flow<ActionStatus<Recipe>>
}

class CreateRecipeUseCase @Inject constructor(
    private val recipeRepo: IRecipeRepo,
    private val pictureRepo: IRecipePictureRepo,
    private val vaultRepo: IEncryptedVaultRepo,
    private val encryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
    private val dispatchers: AppDispatchers,
) : ICreateRecipeUseCase {

    override suspend operator fun invoke(input: RecipeInput): Flow<ActionStatus<Recipe>> =
        withContext(dispatchers.default) {
            flow {
                emit(Loading)

                var inputWithoutPictures = input.withoutPictures()
                var recipeKey: SecretKey? = null
                if (inputWithoutPictures.isEncrypted) {
                    recipeKey = cryptor.generateSymmetricKey()
                    inputWithoutPictures = inputWithoutPictures.encrypt { data -> encryptBytes(data, recipeKey) }
                }

                val result = recipeRepo.createRecipe(inputWithoutPictures, recipeKey)
                if (result.isFailure()) return@flow emit(result)
                val recipeId = result.data().id

                if (recipeKey != null) {
                    val userKeyResult = vaultRepo.getUserPublicKey()
                    if (userKeyResult.isFailure()) return@flow emit(userKeyResult.asFailure())
                    encryptionRepo.setRecipeKey(recipeId, recipeKey, userKeyResult.data())
                }

                var finalInput = pictureRepo.uploadRecipePictures(recipeId, input, recipeKey)
                if (recipeKey != null) {
                    finalInput = finalInput.encrypt { data -> encryptBytes(data, recipeKey) }
                }

                emit(recipeRepo.updateRecipe(recipeId, finalInput, recipeKey))
            }
        }

    private fun encryptBytes(data: ByteArray, recipeKey: SecretKey) =
        Base64.toBase64String(cryptor.encryptDataBySymmetricKey(data, recipeKey))

}
