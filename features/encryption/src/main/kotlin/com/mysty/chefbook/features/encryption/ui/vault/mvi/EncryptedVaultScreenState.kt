package com.mysty.chefbook.features.encryption.ui.vault.mvi

import com.mysty.chefbook.core.android.mvi.MviState
import com.mysty.chefbook.core.constants.Strings

internal sealed class EncryptedVaultScreenState : MviState {

    companion object {
        const val PIN_CODE_LENGTH = 6
    }

    object Loading : EncryptedVaultScreenState()

    object Presentation : EncryptedVaultScreenState()

    data class PinCodeInput(
        val type: PinCodeInputType,
        val pinCode: String = Strings.EMPTY,
    ) : EncryptedVaultScreenState()

    object Management : EncryptedVaultScreenState()

}

internal enum class PinCodeInputType {
    CREATION, VALIDATION, UNLOCKING
}
