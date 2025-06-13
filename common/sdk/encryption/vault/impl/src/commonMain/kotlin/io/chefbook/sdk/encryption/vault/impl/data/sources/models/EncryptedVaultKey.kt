package io.chefbook.sdk.encryption.vault.impl.data.sources.models

class EncryptedVaultKey(
  val key: ByteArray,
  val passwordSalt: ByteArray,
)
