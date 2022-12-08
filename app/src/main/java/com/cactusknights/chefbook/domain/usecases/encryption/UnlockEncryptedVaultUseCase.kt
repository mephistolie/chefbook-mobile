package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import javax.inject.Inject

interface IUnlockEncryptedVaultUseCase {
    suspend operator fun invoke(password: String): SimpleAction
}

class UnlockEncryptedVaultUseCase @Inject constructor(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IUnlockEncryptedVaultUseCase {

    override suspend operator fun invoke(password: String) = encryptionRepo.unlockEncryptedVault(password)

}
