package com.mysty.chefbook.api.encryption.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo

interface ILockEncryptedVaultUseCase {
    suspend operator fun invoke(): SimpleAction
}

internal class LockEncryptedVaultUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
) : ILockEncryptedVaultUseCase {

    override suspend operator fun invoke() = encryptionRepo.lockEncryptedVault()

}
