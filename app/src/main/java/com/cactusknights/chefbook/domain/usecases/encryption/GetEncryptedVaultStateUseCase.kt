package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo

interface IGetEncryptedVaultStateUseCase {
    suspend operator fun invoke(): EncryptedVaultState
}

class GetEncryptedVaultStateUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IGetEncryptedVaultStateUseCase {

    override suspend operator fun invoke() = encryptionRepo.getEncryptedVaultState()

}
