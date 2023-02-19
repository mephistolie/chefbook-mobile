package com.mysty.chefbook.features.auth.ui

import com.mysty.chefbook.api.auth.domain.usecases.ISignInUseCase
import com.mysty.chefbook.api.auth.domain.usecases.ISignUpUseCase
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.Successful
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.auth.R
import com.mysty.chefbook.features.auth.ui.mvi.AuthAction
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenEffect
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenIntent
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenState
import com.mysty.chefbook.features.auth.utils.AuthUtils
import kotlinx.coroutines.flow.MutableStateFlow

internal typealias IAuthViewModel = IMviViewModel<AuthScreenState, AuthScreenIntent, AuthScreenEffect>

internal class AuthViewModel(
    private val signInUseCase: ISignInUseCase,
    private val signUpUseCase: ISignUpUseCase,
) : MviViewModel<AuthScreenState, AuthScreenIntent, AuthScreenEffect>() {

    override val _state: MutableStateFlow<AuthScreenState> = MutableStateFlow(AuthScreenState())

    override suspend fun reduceIntent(intent: AuthScreenIntent) {
        when (intent) {
            is AuthScreenIntent.OpenSignInScreen -> openSignInScreen()
            is AuthScreenIntent.OpenSignUpScreen -> openSignUpScreen()
            is AuthScreenIntent.OpenPasswordResetScreen -> openPasswordResetScreen()
            is AuthScreenIntent.SetEmail -> setEmail(intent.email)
            is AuthScreenIntent.SetPassword -> setPassword(intent.password)
            is AuthScreenIntent.SetPasswordValidation -> setPasswordValidation(intent.validation)
            is AuthScreenIntent.AuthButtonClicked -> processAuthorizedButtonClick()
            is AuthScreenIntent.ChooseLocalMode -> signInLocally()
        }
    }

    private suspend fun openSignInScreen() =
        withSafeState { _state.emit(it.copy(action = AuthAction.SIGN_IN)) }

    private suspend fun openSignUpScreen() =
        withSafeState { _state.emit(it.copy(action = AuthAction.SIGN_UP)) }

    private suspend fun openPasswordResetScreen() =
        withSafeState { _state.emit(it.copy(action = AuthAction.RESET_PASSWORD)) }

    private suspend fun setEmail(email: String) {
        withSafeState {
            _state.emit(it.copy(email = email, isEmailValid = AuthUtils.validateEmail(email)))
        }
    }

    private suspend fun setPassword(password: String) {
        withSafeState { state ->
            _state.emit(state.copy(
                password = password,
                passwordRating = AuthUtils.validatePassword(password, state.passwordValidation)
            ))
        }
    }

    private suspend fun setPasswordValidation(validation: String) {
        withSafeState { state ->
            _state.emit(state.copy(
                passwordValidation = validation,
                passwordRating = AuthUtils.validatePassword(state.password, validation)
            ))
        }
    }

    private suspend fun processAuthorizedButtonClick() {
        val currentState = state.value
        when (currentState.action) {
            AuthAction.SIGN_IN -> signIn(currentState.email, currentState.password)
            AuthAction.SIGN_UP -> signUp(currentState.email, currentState.password)
            AuthAction.RESET_PASSWORD -> TODO()
        }
    }

    private suspend fun signUp(email: String, password: String) {
        signUpUseCase(email, password).collect { result ->
            setLoadingState(isLoading = result is Loading)
            when (result) {
                is Successful -> sendMessage(R.string.common_auth_screen_sign_up_success)
                is Failure -> openErrorDialog(error = result.error)
                else -> Unit
            }
        }
    }

    private suspend fun signIn(email: String, password: String) {
        signInUseCase(email, password).collect { result ->
            setLoadingState(isLoading = result is Loading)
            when (result) {
                is Successful -> {
                    sendMessage(R.string.common_auth_screen_sign_in_success)
                    _effect.emit(AuthScreenEffect.OpenHomeScreen)
                }
                is Failure -> openErrorDialog(error = result.error)
                else -> Unit
            }
        }
    }

    private suspend fun signInLocally() {
        // TODO
    }

    private suspend fun setLoadingState(isLoading: Boolean) {
        withSafeState { _state.emit(it.copy(isLoading = isLoading)) }
    }

    private suspend fun openErrorDialog(error: Throwable?) {
        _effect.emit(AuthScreenEffect.OpenErrorDialog(error = error))
    }

    private suspend fun sendMessage(messageCode: Int) {
        _effect.emit(AuthScreenEffect.ShowMessage(messageCode))
    }
}