package io.chefbook.libs.crypto.encryption.models

import dev.whyoleg.cryptography.serialization.asn1.Der
import dev.whyoleg.cryptography.serialization.asn1.modules.PrivateKeyInfo
import dev.whyoleg.cryptography.serialization.asn1.modules.RsaKeyAlgorithmIdentifier

actual class AsymmetricPrivateKey private constructor(
  private var pkcs8Input: ByteArray?,
  private val pkcs1Input: ByteArray?,
) {

  // Used for iOS encryption, shouldn't be recalculated each time
  val pkcs1: ByteArray by lazy {
    pkcs1Input ?: Der.decodeFromByteArray(
      deserializer = PrivateKeyInfo.serializer(),
      bytes = pkcs8Input!!,
    ).privateKey.also { pkcs8Input = null }
  }

  // Used only for external representation purposes
  private val pkcs8: ByteArray
    get() = pkcs8Input ?: Der.encodeToByteArray(
      serializer = PrivateKeyInfo.serializer(),
      value = PrivateKeyInfo(
        version = 0,
        privateKeyAlgorithm = RsaKeyAlgorithmIdentifier,
        privateKey = pkcs1,
      ),
    )

  actual val raw: ByteArray get() = pkcs8

  companion object {
    fun pkcs8(pkcs8Input: ByteArray?) = AsymmetricPrivateKey(
      pkcs8Input = pkcs8Input,
      pkcs1Input = null,
    )

    fun pkcs1(pkcs1Input: ByteArray?) = AsymmetricPrivateKey(
      pkcs8Input = null,
      pkcs1Input = pkcs1Input,
    )
  }
}

actual fun createAsymmetricPrivateKey(raw: ByteArray) = AsymmetricPrivateKey.pkcs8(raw)
