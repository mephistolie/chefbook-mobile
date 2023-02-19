package com.mysty.chefbook.api.encryption.domain.usecases

import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

interface IObserveEncryptedVaultStateUseCase {
    operator fun invoke(): Flow<EncryptedVaultState?>
}

internal class ObserveEncryptedVaultStateUseCase(
    private val encryptionRepo: IEncryptedVaultRepo,
) : IObserveEncryptedVaultStateUseCase {

    override operator fun invoke() = encryptionRepo.observeEncryptedVaultState()
        .onStart { emit(null) }

}
