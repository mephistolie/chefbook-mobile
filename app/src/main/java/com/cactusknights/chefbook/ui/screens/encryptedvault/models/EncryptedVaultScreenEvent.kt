package com.cactusknights.chefbook.ui.screens.encryptedvault.models

sealed class EncryptedVaultScreenEvent {
    object CreateVault : EncryptedVaultScreenEvent()
    data class AddPinCodeNum(
        val number: Int
    ) : EncryptedVaultScreenEvent()
    object RemovePinCodeNum : EncryptedVaultScreenEvent()
    object LockVault : EncryptedVaultScreenEvent()
    object ChangePinCode : EncryptedVaultScreenEvent()
    object DeleteVault : EncryptedVaultScreenEvent()
    object Close : EncryptedVaultScreenEvent()
}
