package com.cactusknights.chefbook.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.auth.models.AuthAction
import com.cactusknights.chefbook.ui.screens.auth.models.AuthProgress
import com.cactusknights.chefbook.ui.screens.auth.models.AuthScreenEffect
import com.cactusknights.chefbook.ui.screens.auth.models.AuthScreenEvent
import com.cactusknights.chefbook.ui.screens.auth.models.AuthScreenState
import com.mysty.chefbook.api.auth.domain.usecases.ISignInUseCase
import com.mysty.chefbook.api.auth.domain.usecases.ISignUpUseCase
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.core.mvi.EventHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class PasswordStates {
    VALID, SHORT, UPPER, LOWER, NUMBER, SPACE
}

class AuthViewModel(
    private val signInUseCase: ISignInUseCase,
    private val signUpUseCase: ISignUpUseCase,
) : ViewModel(), EventHandler<AuthScreenEvent> {

    private val _authState : MutableStateFlow<AuthScreenState> = MutableStateFlow(AuthScreenState())
    val authState : StateFlow<AuthScreenState> = _authState.asStateFlow()

    private val _effect : MutableSharedFlow<AuthScreenEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect : SharedFlow<AuthScreenEffect> = _effect.asSharedFlow()

    override fun obtainEvent(event: AuthScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is AuthScreenEvent.OpenSignInScreen -> _authState.emit(AuthScreenState(action = AuthAction.SIGN_IN))
                is AuthScreenEvent.OpenSignUpScreen -> _authState.emit(AuthScreenState(action = AuthAction.SIGN_UP))
                is AuthScreenEvent.OpenPasswordResetScreen -> _authState.emit(AuthScreenState(action = AuthAction.RESET_PASSWORD))
                is AuthScreenEvent.CloseDialog -> _authState.emit(authState.value.copy(progress = AuthProgress.INPUT))
                is AuthScreenEvent.SignUp -> signUp(event.email, event.password)
                is AuthScreenEvent.SignIn -> signIn(event.email, event.password)
                is AuthScreenEvent.ResetPassword -> { /* TODO */ }
                is AuthScreenEvent.ChooseLocalMode -> signInLocally()
            }
        }
    }

    private suspend fun signUp(email: String, password: String) {
        signUpUseCase(email, password).collect { result ->
            when (result) {
                is Loading ->
                    _authState.emit(AuthScreenState(
                        action = AuthAction.SIGN_UP,
                        progress = AuthProgress.LOADING
                    ))
                is Successful -> sendMessage(R.string.common_auth_screen_sign_up_success)
                is Failure -> {
                    _authState.emit(AuthScreenState(
                        action = AuthAction.SIGN_UP,
                        progress = AuthProgress.ERROR,
                        error = result.error
                    ))
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password).collect { result ->
                when (result) {
                    is Loading ->
                        _authState.emit(AuthScreenState(
                            action = AuthAction.SIGN_IN,
                            progress = AuthProgress.LOADING
                        ))
                    is Successful -> {
                        sendMessage(R.string.common_auth_screen_sign_in_success)
                        _effect.emit(AuthScreenEffect.SignedIn)
                    }
                    is Failure -> {
                        _authState.emit(AuthScreenState(
                            action = AuthAction.SIGN_IN,
                            progress = AuthProgress.ERROR,
                            error = result.error
                        ))
                    }
                }
            }
        }
    }

    private fun signInLocally() {
        viewModelScope.launch {
//            authUseCases.signInLocally().collect { result ->
//                when (result) {
//                    is ResultSuccessful<*> -> {
//                        sendMessage(R.string.login_successfully)
//                        _effect.emit(AuthScreenEffect.SignedIn)
//                    }
//                    is ResultFailure<*> -> {
//                        _authState.emit(AuthScreenState(
//                            action = AuthAction.SIGN_IN,
//                            progress = AuthProgress.ERROR,
//                            error = result.error
//                        ))
//                    }
//                }
//            }
        }
    }

    private suspend fun sendMessage(messageCode: Int) { _effect.emit(AuthScreenEffect.Message(messageCode)) }
}