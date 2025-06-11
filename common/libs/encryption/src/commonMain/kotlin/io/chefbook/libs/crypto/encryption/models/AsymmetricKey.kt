package io.chefbook.libs.crypto.encryption.models

class AsymmetricKey(
  val public: AsymmetricPublicKey,
  val private: AsymmetricPrivateKey,
)

expect class AsymmetricPublicKey {
  val raw: ByteArray
}

expect fun createAsymmetricPublicKey(
  raw: ByteArray
): AsymmetricPublicKey

expect class AsymmetricPrivateKey {
  val raw: ByteArray
}

expect fun createAsymmetricPrivateKey(
  raw: ByteArray
): AsymmetricPrivateKey
