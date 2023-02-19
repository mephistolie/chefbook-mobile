package com.mysty.chefbook.api.encryption.domain

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import java.security.PrivateKey
import java.security.PublicKey
import kotlinx.coroutines.flow.Flow

internal interface IEncryptedVaultRepo {
    fun observeEncryptedVaultState(): Flow<EncryptedVaultState?>
    suspend fun getEncryptedVaultState(): EncryptedVaultState
    suspend fun refreshEncryptedVaultState()
    suspend fun createEncryptedVault(password: String, salt: ByteArray): SimpleAction
    suspend fun unlockEncryptedVault(password: String, salt: ByteArray): SimpleAction
    suspend fun lockEncryptedVault(): SimpleAction
    suspend fun getUserPublicKey(): ActionStatus<PublicKey>
    suspend fun getUserPrivateKey(): ActionStatus<PrivateKey>
    suspend fun deleteEncryptedVault(): SimpleAction
}
