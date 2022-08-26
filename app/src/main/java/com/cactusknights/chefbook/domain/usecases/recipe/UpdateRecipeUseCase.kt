package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.encrypt
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipePictureRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import java.util.*
import javax.crypto.SecretKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IUpdateRecipeUseCase {
    suspend operator fun invoke(recipeId: Int, input: RecipeInput): Flow<ActionStatus<Recipe>>
}

class UpdateRecipeUseCase @Inject constructor(
    private val recipeRepo: IRecipeRepo,
    private val pictureRepo: IRecipePictureRepo,
    private val vaultRepo: IEncryptedVaultRepo,
    private val encryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
) : IUpdateRecipeUseCase {

    override suspend operator fun invoke(recipeId: Int, input: RecipeInput): Flow<ActionStatus<Recipe>> = flow {
        emit(Loading)
        val encoder = Base64.getEncoder()

        var recipeKey: SecretKey? = null
        if (input.isEncrypted) {
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

        var finalInput = pictureRepo.syncRecipePictures(recipeId, input, recipeKey)
        if (recipeKey != null) {
            finalInput = finalInput.encrypt { data -> encoder.encodeToString(cryptor.encryptDataBySymmetricKey(data, recipeKey)) }
        }

        emit(recipeRepo.updateRecipe(recipeId, finalInput))
    }

}
