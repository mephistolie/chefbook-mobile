package io.chefbook.libs.crypto.encryption.models

import dev.whyoleg.cryptography.serialization.asn1.BitArray
import dev.whyoleg.cryptography.serialization.asn1.Der
import dev.whyoleg.cryptography.serialization.asn1.modules.RsaKeyAlgorithmIdentifier
import dev.whyoleg.cryptography.serialization.asn1.modules.SubjectPublicKeyInfo

actual class AsymmetricPublicKey private constructor(
  private var pkcs8Input: ByteArray?,
  private val pkcs1Input: ByteArray?,
) {

  // Used for iOS encryption, shouldn't be recalculated each time
  val pkcs1: ByteArray by lazy {
    pkcs1Input ?: Der.decodeFromByteArray(
      deserializer = SubjectPublicKeyInfo.serializer(),
      bytes = pkcs8Input!!,
    ).subjectPublicKey.byteArray.also { pkcs8Input = null }
  }

  // Used only for external representation purposes
  private val pkcs8: ByteArray
    get() = pkcs8Input ?: Der.encodeToByteArray(
      serializer = SubjectPublicKeyInfo.serializer(),
      value = SubjectPublicKeyInfo(
        algorithm = RsaKeyAlgorithmIdentifier,
        subjectPublicKey = BitArray(0, pkcs1),
      )
    )

  actual val raw: ByteArray get() = pkcs8

  companion object {
    fun pkcs8(pkcs8Input: ByteArray?) = AsymmetricPublicKey(
      pkcs8Input = pkcs8Input,
      pkcs1Input = null,
    )

    fun pkcs1(pkcs1Input: ByteArray?) = AsymmetricPublicKey(
      pkcs8Input = null,
      pkcs1Input = pkcs1Input,
    )
  }
}

actual fun createAsymmetricPublicKey(raw: ByteArray) = AsymmetricPublicKey.pkcs8(raw)
