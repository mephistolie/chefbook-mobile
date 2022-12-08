package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import javax.inject.Inject

interface IDeleteEncryptedVaultUseCase {
    suspend operator fun invoke(): SimpleAction
}

class DeleteEncryptedVaultUseCase @Inject constructor(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IDeleteEncryptedVaultUseCase {

    override suspend operator fun invoke() = encryptionRepo.lockEncryptedVault()

}
