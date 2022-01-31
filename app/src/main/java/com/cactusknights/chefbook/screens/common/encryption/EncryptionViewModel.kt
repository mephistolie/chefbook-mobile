package com.cactusknights.chefbook.screens.common.encryption

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.domain.usecases.EncryptionUseCases
import com.cactusknights.chefbook.screens.common.encryption.models.EncryptionScreenEvent
import com.cactusknights.chefbook.screens.common.encryption.models.EncryptionScreenState
import com.cactusknights.chefbook.screens.common.encryption.models.EncryptionScreenViewEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EncryptionViewModel @Inject constructor(
    private val encryptionUseCases: EncryptionUseCases,
) : ViewModel(), EventHandler<EncryptionScreenEvent> {

    private val _encryptionState: MutableStateFlow<EncryptionScreenState> = MutableStateFlow(EncryptionScreenState.Disabled)
    val encryptionState: StateFlow<EncryptionScreenState> = _encryptionState.asStateFlow()

    private val _viewEffect: MutableSharedFlow<EncryptionScreenViewEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<EncryptionScreenViewEffect> = _viewEffect.asSharedFlow()

    fun listenToEncryption() {
        viewModelScope.launch {
            encryptionUseCases.getEncryptionState().collect { result ->
                if (result is Result.Success) {
                    when (result.data!!) {
                        EncryptionState.DISABLED -> _encryptionState.emit(EncryptionScreenState.Disabled)
                        EncryptionState.LOCKED -> _encryptionState.emit(EncryptionScreenState.Locked)
                        EncryptionState.UNLOCKED -> _encryptionState.emit(EncryptionScreenState.Unlocked)
                    }
                }
            }
        }
    }

    override fun obtainEvent(event: EncryptionScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is EncryptionScreenEvent.CreateVault -> createVault(event.password, event.passwordVerification)
                is EncryptionScreenEvent.UnlockVault -> unlockVault(event.password)
                is EncryptionScreenEvent.LockVault -> lockVault()
                is EncryptionScreenEvent.DeleteVault -> deleteVault()
            }
        }
    }

    private suspend fun createVault(password: String, passwordVerification: String) {
        if (validatePassword(password, passwordVerification)) {
            encryptionUseCases.createEncryptedVault(password).collect { result ->
                when (result) {
                    is Result.Loading -> _encryptionState.emit(EncryptionScreenState.Unlocking)
                    is Result.Success -> {
                        _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.vault_created))
                        _encryptionState.emit(EncryptionScreenState.Unlocked)
                        _viewEffect.emit(EncryptionScreenViewEffect.Done)
                    }
                    is Result.Error -> {
                        _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.vault_creation_error))
                        _encryptionState.emit(EncryptionScreenState.Disabled)
                    }
                }
            }
        }
    }

    private suspend fun validatePassword(password: String, passwordVerification: String): Boolean {
        val spaceValidator = """^(?=\S+$).+""".toRegex()
        if (password != passwordVerification) {
            _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.password_mismatch))
            return false
        }
        if (password.isNotEmpty() && !password.matches(spaceValidator)) {
            _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.space_password))
            return false
        }
        if (password.length < 4) {
            _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.short_password_vault))
            return false
        }
        return true
    }

    private suspend fun unlockVault(password: String) {
        if (password.isEmpty()) return _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.empty_fields))
        encryptionUseCases.unlockEncryptedStorage(password).collect { result ->
            when (result) {
                is Result.Loading -> _encryptionState.emit(EncryptionScreenState.Unlocking)
                is Result.Success -> {
                    _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.vault_unlocked))
                    _encryptionState.emit(EncryptionScreenState.Unlocked)
                    _viewEffect.emit(EncryptionScreenViewEffect.Done)
                }
                is Result.Error -> {
                    _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.invalid_storage_password))
                    _encryptionState.emit(EncryptionScreenState.Locked)
                }
            }
        }
    }

    private suspend fun lockVault() {
        encryptionUseCases.lockEncryptedStorage().collect { result ->
            when (result) {
                is Result.Loading -> _encryptionState.emit(EncryptionScreenState.Unlocking)
                is Result.Success -> {
                    _encryptionState.emit(EncryptionScreenState.Locked)
                    _viewEffect.emit(EncryptionScreenViewEffect.Done)
                }
                is Result.Error -> {
                    _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.lock_vault_error))
                    _encryptionState.emit(EncryptionScreenState.Unlocked)
                }
            }
        }
    }

    private suspend fun deleteVault() {
        encryptionUseCases.deleteEncryptedStorage().collect { result ->
            when (result) {
                is Result.Loading -> _encryptionState.emit(EncryptionScreenState.Unlocking)
                is Result.Success -> {
                    _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.vault_deleted))
                    _encryptionState.emit(EncryptionScreenState.Disabled)
                    _viewEffect.emit(EncryptionScreenViewEffect.Done)
                }
                is Result.Error -> {
                    _viewEffect.emit(EncryptionScreenViewEffect.Message(R.string.invalid_storage_password))
                    _encryptionState.emit(EncryptionScreenState.Unlocked)
                }
            }
        }
    }
}