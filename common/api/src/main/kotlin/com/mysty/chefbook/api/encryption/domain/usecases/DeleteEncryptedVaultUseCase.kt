package com.mysty.chefbook.api.encryption.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo

interface IDeleteEncryptedVaultUseCase {
    suspend operator fun invoke(): SimpleAction
}

internal class DeleteEncryptedVaultUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IDeleteEncryptedVaultUseCase {

    override suspend operator fun invoke() = encryptionRepo.lockEncryptedVault()

}
