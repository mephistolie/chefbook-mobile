package io.chefbook.libs.crypto.encryption.models

actual class AsymmetricPublicKey(
  actual val raw: ByteArray,
)

actual fun createAsymmetricPublicKey(raw: ByteArray): AsymmetricPublicKey =
  AsymmetricPublicKey(raw = raw)

actual class AsymmetricPrivateKey(
  actual val raw: ByteArray,
)

actual fun createAsymmetricPrivateKey(raw: ByteArray): AsymmetricPrivateKey =
  AsymmetricPrivateKey(raw = raw)
