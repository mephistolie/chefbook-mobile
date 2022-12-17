package com.mysty.chefbook.api.encryption.domain.entities

import java.security.KeyPair

sealed class EncryptedVaultState {

    object Disabled : EncryptedVaultState()

    object Locked : EncryptedVaultState()

    data class Unlocked(
        val keys: KeyPair,
    ) : EncryptedVaultState()

}
