package io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetEncryptedVaultKeyResponse(
  @SerialName("key")
  val key: String? = null,
)
