package io.chefbook.libs.encryption

class AsymmetricKey(
  val public: AsymmetricPublicKey,
  val private: AsymmetricPrivateKey,
)

class AsymmetricPublicKey(
  val raw: ByteArray,
)

class AsymmetricPrivateKey(
  val raw: ByteArray,
)
