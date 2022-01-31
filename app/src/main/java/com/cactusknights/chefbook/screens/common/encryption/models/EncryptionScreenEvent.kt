package com.cactusknights.chefbook.screens.common.encryption.models

sealed class EncryptionScreenEvent {
    data class CreateVault(val password: String, val passwordVerification: String) : EncryptionScreenEvent()
    data class UnlockVault(val password: String) : EncryptionScreenEvent()
    object LockVault : EncryptionScreenEvent()
    object DeleteVault : EncryptionScreenEvent()
}