package com.mysty.chefbook.features.encryption.ui.vault

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.encryption.domain.usecases.ICreateEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IDeleteEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.ILockEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IObserveEncryptedVaultStateUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IUnlockEncryptedVaultUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.core.coroutines.AppDispatchers
import com.mysty.chefbook.features.encryption.R
import com.mysty.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenEffect
import com.mysty.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenIntent
import com.mysty.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenState
import com.mysty.chefbook.features.encryption.ui.vault.mvi.EncryptedVaultScreenState.Companion.PIN_CODE_LENGTH
import com.mysty.chefbook.features.encryption.ui.vault.mvi.PinCodeInputType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal typealias IEncryptedVaultScreenViewModel = IMviViewModel<EncryptedVaultScreenState, EncryptedVaultScreenIntent, EncryptedVaultScreenEffect>

internal class EncryptedVaultScreenViewModel(
  private val closeOnUnlocked: Boolean,

  private val observeEncryptedVaultStateUseCase: IObserveEncryptedVaultStateUseCase,
  private val createEncryptedVaultUseCase: ICreateEncryptedVaultUseCase,
  private val unlockEncryptedVaultUseCase: IUnlockEncryptedVaultUseCase,
  private val lockEncryptedVaultUseCase: ILockEncryptedVaultUseCase,
  private val deleteEncryptedVaultUseCase: IDeleteEncryptedVaultUseCase,
  private val dispatchers: AppDispatchers,
) :
  MviViewModel<EncryptedVaultScreenState, EncryptedVaultScreenIntent, EncryptedVaultScreenEffect>() {

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
          if (encryptionState is EncryptedVaultState.Unlocked && closeOnUnlocked) reduceIntent(
            EncryptedVaultScreenIntent.Close
          )
          _state.emit(
            when (encryptionState) {
              is EncryptedVaultState.Disabled -> EncryptedVaultScreenState.Presentation
              is EncryptedVaultState.Locked -> EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.UNLOCKING)
              is EncryptedVaultState.Unlocked -> EncryptedVaultScreenState.Management
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

    withContext(dispatchers.io) {
      when (createEncryptedVaultUseCase(pinCode)) {
        is Successful -> {
          _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_created))
          _effect.emit(EncryptedVaultScreenEffect.Close)
        }
        is Failure -> {
          _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
          _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_general_something_went_wrong))
        }
        else -> Unit
      }
    }
  }

  private suspend fun unlockVault(pinCode: String) {
    when (unlockEncryptedVaultUseCase(pinCode)) {
      is Successful -> {

        _effect.emit(EncryptedVaultScreenEffect.Close)
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_unlocked))
      }
      is Failure -> {
        _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.UNLOCKING))
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_invalid_pin_code))
      }
      else -> Unit
    }
  }

  private suspend fun lockVault() {
    when (lockEncryptedVaultUseCase()) {
      is Successful -> {
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_locked))
        _effect.emit(EncryptedVaultScreenEffect.Close)
      }
      else -> Unit
    }
  }

  private suspend fun deleteVault() {
    when (deleteEncryptedVaultUseCase()) {
      is Successful -> {
        _effect.emit(EncryptedVaultScreenEffect.ShowToast(R.string.common_encrypted_vault_screen_vault_deleted))
        _effect.emit(EncryptedVaultScreenEffect.Close)
      }
      else -> Unit
    }
  }

}
