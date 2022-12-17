package com.mysty.chefbook.api.encryption.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.safeData
import com.mysty.chefbook.api.common.crypto.AES_SALT_SIZE
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.profile.domain.IProfileRepo
import com.mysty.chefbook.core.sha1

const val DEFAULT_USER_ID = 1

interface ICreateEncryptedVaultUseCase {
    suspend operator fun invoke(password: String): SimpleAction
}

internal class CreateEncryptedVaultUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
    private val profileRepo: IProfileRepo,
) : ICreateEncryptedVaultUseCase {

    override suspend operator fun invoke(password: String): SimpleAction {
        val userId = profileRepo.getProfile().safeData()?.id ?: DEFAULT_USER_ID
        val salt = userId.toString().sha1.toByteArray().copyOf(AES_SALT_SIZE)
        return encryptionRepo.createEncryptedVault(password, salt)
    }

}
