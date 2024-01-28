package io.chefbook.libs.encryption

import io.chefbook.libs.encryption.HybridCryptor.AES_GCM
import io.chefbook.libs.encryption.HybridCryptor.rsaFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

fun SymmetricKey.asSecretKey() =
  SecretKeySpec(raw, AES_GCM)

fun SecretKey.asSymmetricKey() =
  SymmetricKey(encoded)

fun KeyPair.asAsymmetricKey() = AsymmetricKey(
  public = AsymmetricPublicKey(public.encoded),
  private = AsymmetricPrivateKey(private.encoded),
)

fun AsymmetricPublicKey.asPublicKey() =
  rsaFactory.generatePublic(X509EncodedKeySpec(raw))

fun AsymmetricPrivateKey.asPrivateKey() =
  rsaFactory.generatePrivate(PKCS8EncodedKeySpec(raw))
