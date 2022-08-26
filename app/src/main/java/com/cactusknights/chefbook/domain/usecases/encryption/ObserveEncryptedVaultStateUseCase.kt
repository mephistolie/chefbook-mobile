package com.cactusknights.chefbook.domain.usecases.encryption

import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

interface IObserveEncryptedVaultStateUseCase {
    suspend operator fun invoke(): StateFlow<EncryptedVaultState>
}

class ObserveEncryptedVaultStateUseCase @Inject constructor(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IObserveEncryptedVaultStateUseCase {

    override suspend operator fun invoke() = encryptionRepo.observeEncryptedVaultState()

}
