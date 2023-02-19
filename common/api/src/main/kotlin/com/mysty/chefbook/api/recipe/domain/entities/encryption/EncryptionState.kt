package com.mysty.chefbook.api.recipe.domain.entities.encryption

import javax.crypto.SecretKey

sealed class EncryptionState {

    object Standard : EncryptionState()

    object Encrypted : EncryptionState()

    data class Decrypted(
        val key: SecretKey
    ) : EncryptionState()

}
