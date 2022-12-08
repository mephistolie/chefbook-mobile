package com.cactusknights.chefbook.ui.screens.encryptedvault.models

import com.cactusknights.chefbook.common.Strings

sealed class EncryptedVaultScreenState {

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

enum class PinCodeInputType {
    CREATION, VALIDATION, UNLOCKING
}
