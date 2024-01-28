package io.chefbook.features.encryption.ui.vault

import androidx.lifecycle.viewModelScope
import io.chefbook.core.android.R as coreR
import io.chefbook.features.encryption.R
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.CreateEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.DeleteEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.LockEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.ObserveEncryptedVaultStateUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.UnlockEncryptedVaultUseCase
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenEffect
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenIntent
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenState
import io.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenState.Companion.PIN_CODE_LENGTH
import io.chefbook.features.encryption.ui.vault.mvi.PinCodeInputType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IEncryptedVaultScreenViewModel = MviViewModel<EncryptedVaultScreenState, EncryptedVaultScreenIntent, EncryptedVaultScreenEffect>

internal class EncryptedVaultScreenViewModel(
  private val closeOnUnlocked: Boolean,

  private val observeEncryptedVaultStateUseCase: ObserveEncryptedVaultStateUseCase,
  private val createEncryptedVaultUseCase: CreateEncryptedVaultUseCase,
  private val unlockEncryptedVaultUseCase: UnlockEncryptedVaultUseCase,
  private val lockEncryptedVaultUseCase: LockEncryptedVaultUseCase,
  private val deleteEncryptedVaultUseCase: DeleteEncryptedVaultUseCase,
) :
  BaseMviViewModel<EncryptedVaultScreenState, EncryptedVaultScreenIntent, EncryptedVaultScreenEffect>() {

  override val _state: MutableStateFlow<EncryptedVaultScreenState> =
    MutableStateFlow(EncryptedVaultScreenState.Loading)

  private var pinCodeValidation: String? = null

  init {
    observeEncryptionState()
  }

  private fun observeEncryptionState() {
    viewModelScope.launch {
      observeEncryptedVaultStateUseCase()
        .collect { encryptionState ->
          if (encryptionState == EncryptedVaultState.UNLOCKED && closeOnUnlocked) {
            reduceIntent(EncryptedVaultScreenIntent.Close)
          }
          _state.emit(
            when (encryptionState) {
              EncryptedVaultState.DISABLED -> EncryptedVaultScreenState.Presentation
              EncryptedVaultState.LOCKED -> EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.UNLOCKING)
              EncryptedVaultState.UNLOCKED -> EncryptedVaultScreenState.Management
              else -> EncryptedVaultScreenState.Loading
            }
          )
        }
    }
  }

  override suspend fun reduceIntent(intent: EncryptedVaultScreenIntent) {
    when (intent) {
      is EncryptedVaultScreenIntent.CreateVault ->
        _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))

      is EncryptedVaultScreenIntent.AddPinCodeNum -> handlePinCodeNumAdd(intent.number)
      is EncryptedVaultScreenIntent.RemovePinCodeNum -> handlePinCodeNumRemove()
      is EncryptedVaultScreenIntent.LockVault -> lockVault()
      is EncryptedVaultScreenIntent.ChangePinCode -> TODO()
      is EncryptedVaultScreenIntent.DeleteVault -> deleteVault()
      is EncryptedVaultScreenIntent.Back -> {
        pinCodeValidation = null
        _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
      }

      is EncryptedVaultScreenIntent.Close -> _effect.emit(EncryptedVaultScreenEffect.Close)
    }
  }

  private suspend fun handlePinCodeNumAdd(
    number: Int
  ) {
    (state.value as? EncryptedVaultScreenState.PinCodeInput)?.let { currentState ->
      val newState = currentState.copy(pinCode = currentState.pinCode + number.toString())
      _state.emit(newState)
      if (newState.pinCode.length == PIN_CODE_LENGTH) onPinCodeEntered(newState)
    }
  }

  private suspend fun handlePinCodeNumRemove() {
    (state.value as? EncryptedVaultScreenState.PinCodeInput)?.let { currentState ->
      val newState = if (currentState.pinCode.isNotEmpty())
        currentState.copy(
          pinCode = currentState.pinCode.substring(
            0,
            currentState.pinCode.lastIndex
          )
        )
      else
        currentState
      _state.emit(newState)
    }
  }

  private suspend fun onPinCodeEntered(finalState: EncryptedVaultScreenState.PinCodeInput) {
    _state.emit(EncryptedVaultScreenState.Loading)
    when (finalState.type) {
      PinCodeInputType.CREATION -> {
        pinCodeValidation = finalState.pinCode
        _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.VALIDATION))
      }

      PinCodeInputType.VALIDATION -> createVault(finalState.pinCode)
      PinCodeInputType.UNLOCKING -> unlockVault(finalState.pinCode)
    }
  }

  private suspend fun createVault(pinCode: String) {
    if (pinCode != pinCodeValidation) {
      _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_pin_code_mismatch))
      pinCodeValidation = null
      _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
      return
    }

    createEncryptedVaultUseCase(pinCode)
      .onSuccess {
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_created))
        _effect.emit(EncryptedVaultScreenEffect.Close)
      }
      .onFailure {
        _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(coreR.string.common_general_something_went_wrong))
      }
  }

  private suspend fun unlockVault(pinCode: String) {
    unlockEncryptedVaultUseCase(pinCode)
      .onSuccess {
        _effect.emit(EncryptedVaultScreenEffect.Close)
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_unlocked))
      }
      .onFailure {
        _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.UNLOCKING))
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_invalid_pin_code))
      }
  }

  private suspend fun lockVault() {
    lockEncryptedVaultUseCase()
      .onSuccess {
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_locked))
        _effect.emit(EncryptedVaultScreenEffect.Close)
      }
  }

  private suspend fun deleteVault() {
    deleteEncryptedVaultUseCase()
      .onSuccess {
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_deleted))
        _effect.emit(EncryptedVaultScreenEffect.Close)
      }
  }
}
