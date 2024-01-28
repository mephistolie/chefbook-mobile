package io.chefbook.features.encryption.ui.vault.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class EncryptedVaultScreenIntent : MviIntent {
    data object CreateVault : EncryptedVaultScreenIntent()
    data class AddPinCodeNum(
        val number: Int
    ) : EncryptedVaultScreenIntent()
    data object RemovePinCodeNum : EncryptedVaultScreenIntent()
    data object LockVault : EncryptedVaultScreenIntent()
    data object ChangePinCode : EncryptedVaultScreenIntent()
    data object DeleteVault : EncryptedVaultScreenIntent()
    data object Back : EncryptedVaultScreenIntent()
    data object Close : EncryptedVaultScreenIntent()
}
