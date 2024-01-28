package io.chefbook.features.encryption.ui.vault.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class EncryptedVaultScreenEffect : MviSideEffect {
    data class ShowToast(val messageId: Int) : EncryptedVaultScreenEffect()
    data object Close : EncryptedVaultScreenEffect()
}
