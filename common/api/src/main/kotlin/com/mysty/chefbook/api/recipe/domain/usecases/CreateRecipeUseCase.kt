package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.crypto.ICryptor
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.recipe.data.crypto.RecipeCryptor
import com.mysty.chefbook.api.recipe.domain.IRecipeEncryptionRepo
import com.mysty.chefbook.api.recipe.domain.IRecipePictureRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.withoutPictures
import com.mysty.chefbook.core.coroutines.AppDispatchers
import javax.crypto.SecretKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface ICreateRecipeUseCase {
    suspend operator fun invoke(input: RecipeInput): Flow<ActionStatus<Recipe>>
}

internal class CreateRecipeUseCase(
    private val recipeRepo: IRecipeRepo,
    private val pictureRepo: IRecipePictureRepo,
    private val vaultRepo: IEncryptedVaultRepo,
    private val encryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
    private val recipeCryptor: RecipeCryptor,
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
                    inputWithoutPictures = recipeCryptor.encryptRecipe(inputWithoutPictures, recipeKey)
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
                    finalInput = recipeCryptor.encryptRecipe(finalInput, recipeKey)
                }

                emit(recipeRepo.updateRecipe(recipeId, finalInput, recipeKey))
            }
        }

}
