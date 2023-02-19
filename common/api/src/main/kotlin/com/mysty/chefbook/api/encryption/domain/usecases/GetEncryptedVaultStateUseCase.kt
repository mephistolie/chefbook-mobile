package com.mysty.chefbook.api.encryption.domain.usecases

import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState

interface IGetEncryptedVaultStateUseCase {
    suspend operator fun invoke(): EncryptedVaultState
}

internal class GetEncryptedVaultStateUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IGetEncryptedVaultStateUseCase {

    override suspend operator fun invoke() = encryptionRepo.getEncryptedVaultState()

}
