package io.chefbook.sdk.encryption.vault.impl.data.sources.remote

import io.chefbook.libs.crypto.encryption.models.AsymmetricPublicKey
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.encryption.vault.impl.data.sources.models.EncryptedVaultKey
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.EncryptedVaultApiService
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto.CreateEncryptedVaultRequest
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64

internal class RemoteEncryptedVaultSourceImpl(
  private val api: EncryptedVaultApiService,
) : RemoteEncryptedVaultSource {

  override suspend fun getEncryptedVaultKey() =
    api.getEncryptedVaultKey().mapCatching { response ->
      if (response.key == null || response.passwordSalt == null) throw NotFoundException()

      EncryptedVaultKey(
        key = response.key.decodeBase64Bytes(),
        passwordSalt = response.passwordSalt.decodeBase64Bytes(),
      )
    }

  override suspend fun createEncryptedVault(
    publicKey: AsymmetricPublicKey,
    privateKey: ByteArray,
    salt: ByteArray,
  ) =
    api.createEncryptedVault(
      CreateEncryptedVaultRequest(
        publicKey = publicKey.raw.encodeBase64(),
        privateKey = privateKey.encodeBase64(),
        salt = salt.encodeBase64(),
      )
    ).asEmpty()

  override suspend fun deleteEncryptedVault() =
    api.requestEncryptedVaultDeletion().asEmpty()
}
