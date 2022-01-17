package com.cactusknights.chefbook.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.AuthUseCases
import com.cactusknights.chefbook.screens.auth.models.AuthViewEffect
import com.cactusknights.chefbook.screens.auth.models.AuthEvent
import com.cactusknights.chefbook.screens.auth.models.AuthScreenState
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
) : ViewModel(), EventHandler<AuthEvent> {

    private val _authState : MutableStateFlow<AuthScreenState> = MutableStateFlow(AuthScreenState.SignInScreen())
    val authState : StateFlow<AuthScreenState> = _authState.asStateFlow()

    private val _viewEffect : MutableSharedFlow<AuthViewEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect : SharedFlow<AuthViewEffect> = _viewEffect.asSharedFlow()

    override fun obtainEvent(event: AuthEvent) {
        viewModelScope.launch {
            when (event) {
                is AuthEvent.SignInSelected -> _authState.emit(AuthScreenState.SignInScreen())
                is AuthEvent.SignUpScreen -> _authState.emit(AuthScreenState.SignUpScreen())
                is AuthEvent.RestorePasswordScreen -> _authState.emit(AuthScreenState.RestorePasswordScreen())
                else -> { if (!_authState.value.isLoading) reduceAuthEvent(event) }
            }
        }
    }

    private fun reduceAuthEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.SignUp -> signUp(event.email, event.password, event.passwordValidation)
            is AuthEvent.SignIn -> signIn(event.email, event.password)
            is AuthEvent.RestorePassword -> { /* TODO */ }
            is AuthEvent.SignInLocally -> signInLocally()
        }
    }

    private fun signUp(email: String, password: String, passwordValidation: String) {
        viewModelScope.launch {
            if (checkAuthFields(email, password, passwordValidation)) {
                authUseCases.signUp(email, password).collect { result ->
                    when (result) {
                        is Result.Loading -> _authState.emit(AuthScreenState.SignUpScreen(isLoading = true))
                        is Result.Success -> sendMessage(R.string.signup_successfully)
                        is Result.Error -> {
                            AuthScreenState.SignUpScreen()
                            sendMessage(R.string.authentication_failed)
                        }
                    }
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.signIn(email, password).collect { result ->
                when (result) {
                    is Result.Loading -> { _authState.emit(AuthScreenState.SignInScreen(isLoading = true)) }
                    is Result.Success -> {
                        sendMessage(R.string.login_successfully)
                        _viewEffect.emit(AuthViewEffect.SignedIn) }
                    is Result.Error -> {
                        _authState.emit(AuthScreenState.SignInScreen())
                        sendMessage(R.string.authentication_failed)
                    }
                }
            }
        }
    }

    private fun signInLocally() {
        viewModelScope.launch {
            authUseCases.signInLocally().collect {
                if (it is Result.Success) {
                    sendMessage(R.string.login_successfully)
                    _viewEffect.emit(AuthViewEffect.SignedIn)
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
            sendMessage(R.string.empty_fields)
            return false
        }
        if (!email.contains('@') || !email.contains('.')) {
            sendMessage(R.string.invalid_email)
            return false
        }
        if (passwordText != repeatPasswordText && repeatPasswordText.isNotEmpty()) {
            sendMessage(R.string.password_mismatch)
            return false
        }
        when (validatePassword(passwordText)) {
            PasswordStates.SPACE -> {
                sendMessage(R.string.space_password)
            }
            PasswordStates.SHORT -> {
                sendMessage(R.string.short_password)
            }
            PasswordStates.LOWER -> {
                sendMessage(R.string.lower_password)
            }
            PasswordStates.UPPER -> {
                sendMessage(R.string.upper_password)
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

    private suspend fun sendMessage(messageCode: Int) { _viewEffect.emit(AuthViewEffect.Message(messageCode)) }
}