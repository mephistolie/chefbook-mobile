package io.chefbook.libs.encryption

interface AsymmetricKey {
  val public: AsymmetricPublicKey
  val private: AsymmetricPrivateKey
}

interface AsymmetricPublicKey {

  val serialized: ByteArray
}

interface AsymmetricPrivateKey {

  val public: AsymmetricPublicKey
}
