package com.mysty.chefbook.features.encryption.ui.vault.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class EncryptedVaultScreenIntent : MviIntent {
    object CreateVault : EncryptedVaultScreenIntent()
    data class AddPinCodeNum(
        val number: Int
    ) : EncryptedVaultScreenIntent()
    object RemovePinCodeNum : EncryptedVaultScreenIntent()
    object LockVault : EncryptedVaultScreenIntent()
    object ChangePinCode : EncryptedVaultScreenIntent()
    object DeleteVault : EncryptedVaultScreenIntent()
    object Back : EncryptedVaultScreenIntent()
    object Close : EncryptedVaultScreenIntent()
}
