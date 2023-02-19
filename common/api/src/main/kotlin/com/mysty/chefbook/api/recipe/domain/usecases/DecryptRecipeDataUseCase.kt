package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.crypto.ICryptor
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeEncryptionRepo
import kotlinx.coroutines.runBlocking

interface IDecryptRecipeDataUseCase {
    operator fun invoke(data: ByteArray, recipeId: String): ByteArray
}

internal class DecryptRecipeDataUseCase(
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
