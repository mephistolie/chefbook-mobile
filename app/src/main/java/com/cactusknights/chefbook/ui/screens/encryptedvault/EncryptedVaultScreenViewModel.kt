package com.cactusknights.chefbook.ui.screens.encryptedvault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.encryption.domain.usecases.ICreateEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IDeleteEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.ILockEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IObserveEncryptedVaultStateUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IUnlockEncryptedVaultUseCase
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenEffect
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenEvent
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenState
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.EncryptedVaultScreenState.Companion.PIN_CODE_LENGTH
import com.cactusknights.chefbook.ui.screens.encryptedvault.models.PinCodeInputType
import com.mysty.chefbook.core.coroutines.AppDispatchers
import com.mysty.chefbook.core.mvi.EventHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EncryptedVaultScreenViewModel(
    private val observeEncryptedVaultStateUseCase: IObserveEncryptedVaultStateUseCase,
    private val createEncryptedVaultUseCase: ICreateEncryptedVaultUseCase,
    private val unlockEncryptedVaultUseCase: IUnlockEncryptedVaultUseCase,
    private val lockEncryptedVaultUseCase: ILockEncryptedVaultUseCase,
    private val deleteEncryptedVaultUseCase: IDeleteEncryptedVaultUseCase,
    private val dispatchers: AppDispatchers,
) : ViewModel(), EventHandler<EncryptedVaultScreenEvent> {

    private val _state: MutableStateFlow<EncryptedVaultScreenState> =
        MutableStateFlow(EncryptedVaultScreenState.Loading)
    val state: StateFlow<EncryptedVaultScreenState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<EncryptedVaultScreenEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<EncryptedVaultScreenEffect> = _effect.asSharedFlow()

    private var closeOnUnlocked: Boolean = false

    private var pinCodeValidation: String? = null

    init {
        observeEncryptionState()
    }

    private fun observeEncryptionState() {
        viewModelScope.launch {
            observeEncryptedVaultStateUseCase()
                .onEach { encryptionState ->
                    if (encryptionState is EncryptedVaultState.Unlocked && closeOnUnlocked) obtainEvent(EncryptedVaultScreenEvent.Close)
                    _state.emit(when (encryptionState) {
                        is EncryptedVaultState.Disabled -> EncryptedVaultScreenState.Presentation
                        is EncryptedVaultState.Locked -> EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.UNLOCKING)
                        is EncryptedVaultState.Unlocked -> EncryptedVaultScreenState.Management
                    })
                }
                .collect()
        }
    }

    override fun obtainEvent(event: EncryptedVaultScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is EncryptedVaultScreenEvent.CreateVault -> _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
                is EncryptedVaultScreenEvent.AddPinCodeNum -> handlePinCodeNumAdd(event.number)
                is EncryptedVaultScreenEvent.RemovePinCodeNum -> handlePinCodeNumRemove()
                is EncryptedVaultScreenEvent.LockVault -> lockVault()
                is EncryptedVaultScreenEvent.ChangePinCode -> TODO()
                is EncryptedVaultScreenEvent.DeleteVault -> deleteVault()
                is EncryptedVaultScreenEvent.Back -> {
                    pinCodeValidation = null
                    _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
                }
                is EncryptedVaultScreenEvent.Close -> _effect.emit(EncryptedVaultScreenEffect.OnClose)
                is EncryptedVaultScreenEvent.CloseOnUnlocked -> {
                    closeOnUnlocked = true
                    if (state.value is EncryptedVaultScreenState.Management) obtainEvent(EncryptedVaultScreenEvent.Close)
                }
            }
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
                currentState.copy(pinCode = currentState.pinCode.substring(0, currentState.pinCode.lastIndex))
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
            _effect.emit(EncryptedVaultScreenEffect.Toast(R.string.common_encrypted_vault_screen_pin_code_mismatch))
            pinCodeValidation = null
            _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
            return
        }

        withContext(dispatchers.io) {
            when (createEncryptedVaultUseCase(pinCode)) {
                is Successful -> {
                    _effect.emit(EncryptedVaultScreenEffect.Toast(R.string.common_encrypted_vault_screen_vault_created))
                    _effect.emit(EncryptedVaultScreenEffect.OnClose)
                }
                is Failure -> {
                    _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.CREATION))
                    _effect.emit(EncryptedVaultScreenEffect.Toast(R.string.common_general_something_went_wrong))
                }
                else -> Unit
            }
        }
    }

    private suspend fun unlockVault(pinCode: String) {
        when (unlockEncryptedVaultUseCase(pinCode)) {
            is Successful -> {

                _effect.emit(EncryptedVaultScreenEffect.OnClose)
                _effect.emit(EncryptedVaultScreenEffect.Toast(R.string.common_encrypted_vault_screen_vault_unlocked))
            }
            is Failure -> {
                _state.emit(EncryptedVaultScreenState.PinCodeInput(type = PinCodeInputType.UNLOCKING))
                _effect.emit(EncryptedVaultScreenEffect.Toast(R.string.common_encrypted_vault_screen_invalid_pin_code))
            }
            else -> Unit
        }
    }

    private suspend fun lockVault() {
        when (lockEncryptedVaultUseCase()) {
            is Successful -> {
                _effect.emit(EncryptedVaultScreenEffect.Toast(R.string.common_encrypted_vault_screen_vault_locked))
                _effect.emit(EncryptedVaultScreenEffect.OnClose)
            }
            else -> Unit
        }
    }

    private suspend fun deleteVault() {
        when (deleteEncryptedVaultUseCase()) {
            is Successful -> {
                _effect.emit(EncryptedVaultScreenEffect.Toast(R.string.common_encrypted_vault_screen_vault_deleted))
                _effect.emit(EncryptedVaultScreenEffect.OnClose)
            }
            else -> Unit
        }
    }

}
