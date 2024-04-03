package io.chefbook.libs.encryption

import com.google.crypto.tink.KeysetHandle

class SymmetricKeyImpl(
  val keyset: KeysetHandle,
) : SymmetricKey
