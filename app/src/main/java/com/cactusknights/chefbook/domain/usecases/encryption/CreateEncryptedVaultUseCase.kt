package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import javax.inject.Inject

interface ICreateEncryptedVaultUseCase {
    suspend operator fun invoke(password: String): SimpleAction
}

class CreateEncryptedVaultUseCase @Inject constructor(
    private val encryptionRepo: IEncryptedVaultRepo,
) : ICreateEncryptedVaultUseCase {

    override suspend operator fun invoke(password: String) =
        encryptionRepo.createEncryptedVault(password)

}
