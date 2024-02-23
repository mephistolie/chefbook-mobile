package io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services

import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.CreateEncryptedVaultRequest
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.DeleteEncryptedVaultRequest
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.GetEncryptedVaultKeyResponse
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class EncryptedVaultApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), EncryptedVaultApiService {

  override suspend fun getEncryptedVaultKey(): Result<GetEncryptedVaultKeyResponse> =
    safeGet(ENCRYPTED_VAULT_ROUTE)

  override suspend fun createEncryptedVault(body: CreateEncryptedVaultRequest): Result<MessageResponse> =
    safePost(ENCRYPTED_VAULT_ROUTE) { setBody(body) }

  override suspend fun requestEncryptedVaultDeletion(): Result<MessageResponse> =
    safePost("$ENCRYPTED_VAULT_ROUTE/delete")

  override suspend fun deleteEncryptedVault(body: DeleteEncryptedVaultRequest): Result<MessageResponse> =
    safeDelete("$ENCRYPTED_VAULT_ROUTE/delete") { setBody(body) }

  companion object {
    private const val ENCRYPTED_VAULT_ROUTE = "v1/encryption/vault"
  }
}
