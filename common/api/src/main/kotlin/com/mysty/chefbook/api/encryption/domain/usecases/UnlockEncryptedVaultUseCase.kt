package com.mysty.chefbook.api.encryption.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.common.crypto.AES_SALT_SIZE
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.profile.domain.IProfileRepo
import com.mysty.chefbook.core.sha1

interface IUnlockEncryptedVaultUseCase {
    suspend operator fun invoke(password: String): SimpleAction
}

internal class UnlockEncryptedVaultUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
    private val profileRepo: IProfileRepo,
) : IUnlockEncryptedVaultUseCase {

    override suspend operator fun invoke(password: String): SimpleAction {
        val userId = (profileRepo.getProfile() as? Successful)?.data?.id ?: DEFAULT_USER_ID
        val salt = userId.toString().sha1.toByteArray().copyOf(AES_SALT_SIZE)
        return encryptionRepo.unlockEncryptedVault(password, salt)
    }

}
