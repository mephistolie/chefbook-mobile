package io.chefbook.features.encryption.ui.vault.mvi

import io.chefbook.libs.mvi.MviState

internal sealed class EncryptedVaultScreenState : MviState {

  companion object {
    const val PIN_CODE_LENGTH = 6
  }

  data object Loading : EncryptedVaultScreenState()

  data object Presentation : EncryptedVaultScreenState()

  data class PinCodeInput(
    val type: PinCodeInputType,
    val pinCode: String = "",
  ) : EncryptedVaultScreenState()

  data object Management : EncryptedVaultScreenState()

}

internal enum class PinCodeInputType {
  CREATION, VALIDATION, UNLOCKING
}
