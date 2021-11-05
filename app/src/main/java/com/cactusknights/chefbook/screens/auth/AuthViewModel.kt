package com.cactusknights.chefbook.screens.auth

import android.content.res.loader.ResourcesProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import com.cactusknights.chefbook.enums.SignStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthActivityState())
    val state: StateFlow<AuthActivityState> = _state

    private val _message = MutableStateFlow(Any())
    val message: Flow<Any> = _message

    fun updateState(newState: AuthActivityState) {
        _state.value = newState
    }

    fun signUp(email: String, password: String) {
        authUseCases.signUp(email, password).onEach { result ->
            when (result) {
                is Result.Loading -> {
                    _state.value = AuthActivityState(
                        inProgress = true,
                        authState = SignStates.SIGN_UP
                    )
                }
                is Result.Success -> {
                    _message.value = R.string.signup_successfully
                    _state.value = AuthActivityState(
                        authState = SignStates.SIGN_IN,
                        inProgress = false
                    )
                }
                is Result.Error -> {
                    _message.value = R.string.authentication_failed
                    _state.value = AuthActivityState(
                        authState = SignStates.SIGN_UP,
                        inProgress = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signIn(email: String, password: String) {
        authUseCases.signIn(email, password).onEach { result ->
            when (result) {
                is Result.Loading -> {
                    _state.value = AuthActivityState(
                        inProgress = true
                    )
                }
                is Result.Success -> {
                    _message.value = R.string.login_successfully
                    _state.value = AuthActivityState(
                        authState = SignStates.SIGNED_IN,
                        inProgress = false
                    )
                }
                is Result.Error -> {
                    _message.value = R.string.authentication_failed
                    _state.value = AuthActivityState(
                        inProgress = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

//    fun signInEmail(email: String, password: String, callback: (isLoggedIn: Boolean) -> Unit) { viewModelScope.launch {  authRepository.signInEmail(email, password, callback) } }
//    fun signGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit) { viewModelScope.launch {  authRepository.signInGoogle(idToken, callback) } }
//    fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit) { viewModelScope.launch { authRepository.restorePassword(email, callback) } }
//    fun signOut() { viewModelScope.launch { authRepository.logout() } }
}