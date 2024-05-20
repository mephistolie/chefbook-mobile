package io.chefbook.libs.encryption

import com.google.crypto.tink.KeysetHandle

fun SymmetricKey.asSecretKey() =
  (this as SymmetricKeyImpl).keyset

fun KeysetHandle.asSymmetricKey() =
  SymmetricKeyImpl(this)

fun KeysetHandle.asAsymmetricKey(): AsymmetricKey {
  val privateKeyset = this

  return object : AsymmetricKey {

    override val public: AsymmetricPublicKey
      get() = AsymmetricPublicKeyImpl(privateKeyset.publicKeysetHandle)

    override val private: AsymmetricPrivateKey
      get() = AsymmetricPrivateKeyImpl(privateKeyset)
  }
}

fun AsymmetricPublicKey.asPublicKey() =
  (this as AsymmetricPublicKeyImpl).keyset

fun AsymmetricPrivateKey.asPrivateKey() =
  (this as AsymmetricPrivateKeyImpl).keyset
