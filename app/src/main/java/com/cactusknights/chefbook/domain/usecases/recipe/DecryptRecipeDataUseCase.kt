package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

interface IDecryptRecipeDataUseCase {
    operator fun invoke(data: ByteArray, recipeId: String): ByteArray
}

class DecryptRecipeDataUseCase @Inject constructor(
    private val encryptedVaultRepo: IEncryptedVaultRepo,
    private val recipeEncryptionRepo: IRecipeEncryptionRepo,
    private val cryptor: ICryptor,
) : IDecryptRecipeDataUseCase {

    override operator fun invoke(data: ByteArray, recipeId: String) = runBlocking {
        val userKey = encryptedVaultRepo.getUserPrivateKey()
        if (userKey.isFailure()) {
            return@runBlocking data
        }

        val recipeKey = recipeEncryptionRepo.getRecipeKey(recipeId, userKey.data())
        if (recipeKey.isFailure()) {
            return@runBlocking data
        }

        return@runBlocking cryptor.decryptDataBySymmetricKey(data, recipeKey.data())
    }

}
