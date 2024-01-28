package io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services

import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.CreateEncryptedVaultRequest
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.DeleteEncryptedVaultRequest
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.GetEncryptedVaultKeyResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface EncryptedVaultApiService {

  suspend fun getEncryptedVaultKey(): Result<GetEncryptedVaultKeyResponse>

  suspend fun createEncryptedVault(body: CreateEncryptedVaultRequest): Result<MessageResponse>

  suspend fun requestEncryptedVaultDeletion(): Result<MessageResponse>

  suspend fun deleteEncryptedVault(body: DeleteEncryptedVaultRequest): Result<MessageResponse>
}
