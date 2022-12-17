package com.mysty.chefbook.api.encryption.domain.usecases

import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import kotlinx.coroutines.flow.StateFlow

interface IObserveEncryptedVaultStateUseCase {
    suspend operator fun invoke(): StateFlow<EncryptedVaultState>
}

internal class ObserveEncryptedVaultStateUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IObserveEncryptedVaultStateUseCase {

    override suspend operator fun invoke() = encryptionRepo.observeEncryptedVaultState()

}
