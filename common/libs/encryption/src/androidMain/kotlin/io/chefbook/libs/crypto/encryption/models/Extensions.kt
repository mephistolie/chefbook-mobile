package io.chefbook.libs.crypto.encryption.models

import io.chefbook.libs.crypto.encryption.Aes
import io.chefbook.libs.crypto.encryption.HybridCryptor.rsaFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

internal fun SymmetricKey.asSecretKey() =
  SecretKeySpec(raw, Aes)

internal fun SecretKey.asSymmetricKey() =
  SymmetricKey(encoded)

internal fun KeyPair.asAsymmetricKey() = AsymmetricKey(
  public = AsymmetricPublicKey(public.encoded),
  private = AsymmetricPrivateKey(private.encoded),
)

internal fun AsymmetricPublicKey.asPublicKey() =
  rsaFactory.generatePublic(X509EncodedKeySpec(raw))

internal fun AsymmetricPrivateKey.asPrivateKey() =
  rsaFactory.generatePrivate(PKCS8EncodedKeySpec(raw))
