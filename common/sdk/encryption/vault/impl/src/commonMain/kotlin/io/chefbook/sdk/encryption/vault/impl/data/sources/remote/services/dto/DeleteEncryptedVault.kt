package io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DeleteEncryptedVaultRequest(
  @SerialName("deleteCode")
  val deleteCode: String,
)
