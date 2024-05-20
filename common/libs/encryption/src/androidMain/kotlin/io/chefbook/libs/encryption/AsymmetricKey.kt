package io.chefbook.libs.encryption

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat

class AsymmetricPublicKeyImpl(
  val keyset: KeysetHandle,
) : AsymmetricPublicKey {

  override val serialized: ByteArray
    get() = TinkProtoKeysetFormat.serializeKeysetWithoutSecret(keyset)
}

class AsymmetricPrivateKeyImpl(
  val keyset: KeysetHandle,
) : AsymmetricPrivateKey {

  override val public
    get() = AsymmetricPublicKeyImpl(keyset.publicKeysetHandle)
}
