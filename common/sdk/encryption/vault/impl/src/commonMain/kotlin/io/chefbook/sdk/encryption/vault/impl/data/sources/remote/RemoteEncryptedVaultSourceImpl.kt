package io.chefbook.sdk.encryption.vault.impl.data.sources.remote

import io.chefbook.libs.encryption.AsymmetricPublicKey
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.EncryptedVaultApiService
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.CreateEncryptedVaultRequest
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64

internal class RemoteEncryptedVaultSourceImpl(
  private val api: EncryptedVaultApiService,
) : RemoteEncryptedVaultSource {

  override suspend fun getEncryptedVaultKey() =
    api.getEncryptedVaultKey().fold(
      onSuccess = { response ->
        if (response.key == null) return@fold Result.failure(NotFoundException())
        Result.success(response.key.decodeBase64Bytes())
      },
      onFailure = { Result.failure(it) }
    )

  override suspend fun createEncryptedVault(
    publicKey: AsymmetricPublicKey,
    privateKey: ByteArray
  ) =
    api.createEncryptedVault(
      CreateEncryptedVaultRequest(
        publicKey = publicKey.serialized.encodeBase64(),
        privateKey = privateKey.encodeBase64(),
      )
    ).asEmpty()

  override suspend fun deleteEncryptedVault() =
    api.requestEncryptedVaultDeletion().asEmpty()
}
