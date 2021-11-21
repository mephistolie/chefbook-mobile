package com.cactusknights.chefbook.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.BaseViewModel
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PasswordStates {
    VALID, SHORT, UPPER, LOWER, NUMBER, SPACE
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
) : BaseViewModel<AuthActivityState>(AuthActivityState()) {

    fun updateState(newState: AuthActivityState) {
        _state.value = newState
    }

    fun signUp(email: String, password: String, passwordValidation: String) {
        viewModelScope.launch {
            if (checkAuthFields(email, password, passwordValidation)) {
                authUseCases.signUp(email, password).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _state.emit(AuthActivityState(
                                inProgress = true,
                                authState = SignStates.SIGN_UP
                            ))
                        }
                        is Result.Success -> {
                            _state.emit(AuthActivityState(
                                authState = SignStates.SIGN_IN,
                                inProgress = false,
                                message = R.string.signup_successfully
                            ))
                        }
                        is Result.Error -> {
                            _state.emit(AuthActivityState(
                                authState = SignStates.SIGN_UP,
                                inProgress = false,
                                message = R.string.authentication_failed
                            ))
                        }
                    }
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            if (checkAuthFields(email, password, password)) {
                authUseCases.signIn(email, password).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _state.value = AuthActivityState(
                                inProgress = true
                            )
                        }
                        is Result.Success -> {
                            _state.value = AuthActivityState(
                                authState = SignStates.SIGNED_IN,
                                inProgress = false,
                                message = R.string.login_successfully
                            )
                        }
                        is Result.Error -> {
                            _state.value = AuthActivityState(
                                inProgress = false,
                                message = R.string.authentication_failed
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun checkAuthFields(
        email: String,
        passwordText: String,
        repeatPasswordText: String
    ): Boolean {
        if (email.isEmpty() || passwordText.isEmpty()) {
            sendErrorMessage(R.string.empty_fields)
            return false
        }
        if (!email.contains('@') || !email.contains('.')) {
            sendErrorMessage(R.string.invalid_email)
            return false
        }
        if (passwordText != repeatPasswordText && repeatPasswordText.isNotEmpty()) {
            sendErrorMessage(R.string.password_mismatch)
            return false
        }
        when (validatePassword(passwordText)) {
            PasswordStates.SPACE -> {
                sendErrorMessage(R.string.space_password)
            }
            PasswordStates.SHORT -> {
                sendErrorMessage(R.string.short_password)
            }
            PasswordStates.LOWER -> {
                sendErrorMessage(R.string.lower_password)
            }
            PasswordStates.UPPER -> {
                sendErrorMessage(R.string.upper_password)
            }
            else -> return true
        }
        return false
    }

    private fun validatePassword(password: String): PasswordStates {

        val numberValidator = """^(?=.*[0-9]).{8,}$""".toRegex()
        val lowerValidator = """^(?=.*[a-z]).{8,}$""".toRegex()
        val upperValidator = """^(?=.*[A-Z]).{8,}$""".toRegex()
        val spaceValidator = """^(?=\S+$).+""".toRegex()

        if (password.isNotEmpty() && !password.matches(spaceValidator)) return PasswordStates.SPACE
        if (password.length < 8) return PasswordStates.SHORT
        if (!password.matches(lowerValidator)) return PasswordStates.LOWER
        if (!password.matches(upperValidator)) return PasswordStates.UPPER
        if (!password.matches(numberValidator)) return PasswordStates.NUMBER

        return PasswordStates.VALID
    }

    private suspend fun sendErrorMessage(messageCode: Int) {
        _state.emit(
            AuthActivityState(
                authState = state.value.authState,
                inProgress = false,
                message = messageCode
            )
        )
    }
}