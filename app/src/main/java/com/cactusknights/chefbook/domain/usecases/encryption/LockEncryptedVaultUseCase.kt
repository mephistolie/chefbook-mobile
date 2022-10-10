package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import javax.inject.Inject

interface ILockEncryptedVaultUseCase {
    suspend operator fun invoke(): SimpleAction
}

class LockEncryptedVaultUseCase @Inject constructor(
    private val encryptionRepo: IEncryptedVaultRepo,
) : ILockEncryptedVaultUseCase {

    override suspend operator fun invoke() = encryptionRepo.lockEncryptedVault()

}
