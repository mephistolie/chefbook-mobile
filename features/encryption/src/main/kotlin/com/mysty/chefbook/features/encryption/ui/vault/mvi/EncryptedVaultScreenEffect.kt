package com.mysty.chefbook.features.encryption.ui.vault.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class EncryptedVaultScreenEffect : MviSideEffect {
    data class ShowToast(val messageId: Int) : EncryptedVaultScreenEffect()
    object Close : EncryptedVaultScreenEffect()
}
