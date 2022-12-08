package com.cactusknights.chefbook.ui.screens.encryptedvault.models

sealed class EncryptedVaultScreenEffect {
    data class Toast(val messageId: Int) : EncryptedVaultScreenEffect()
    object OnClose : EncryptedVaultScreenEffect()
}
