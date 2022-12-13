package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo

interface ILockEncryptedVaultUseCase {
    suspend operator fun invoke(): SimpleAction
}

class LockEncryptedVaultUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
) : ILockEncryptedVaultUseCase {

    override suspend operator fun invoke() = encryptionRepo.lockEncryptedVault()

}
