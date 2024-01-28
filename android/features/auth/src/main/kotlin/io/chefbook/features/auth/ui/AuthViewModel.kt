package io.chefbook.features.auth.ui

import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.features.auth.R
import io.chefbook.features.auth.ui.mvi.AuthAction
import io.chefbook.features.auth.ui.mvi.AuthScreenEffect
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.libs.utils.auth.isEmail
import io.chefbook.libs.utils.auth.validatePassword
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal typealias IAuthViewModel = MviViewModel<AuthScreenState, AuthScreenIntent, AuthScreenEffect>

internal class AuthViewModel(
  private val signUpUseCase: SignUpUseCase,
  private val signInUseCase: SignInUseCase,
) : BaseMviViewModel<AuthScreenState, AuthScreenIntent, AuthScreenEffect>() {

  override val _state: MutableStateFlow<AuthScreenState> = MutableStateFlow(AuthScreenState())

  override suspend fun reduceIntent(intent: AuthScreenIntent) {
    when (intent) {
      is AuthScreenIntent.OpenSignInForm -> openSignInScreen()
      is AuthScreenIntent.OpenSignUpForm -> openSignUpScreen()
      is AuthScreenIntent.OpenPasswordResetScreen -> openPasswordResetScreen()
      is AuthScreenIntent.SetEmail -> setEmail(intent.email)
      is AuthScreenIntent.SetPassword -> setPassword(intent.password)
      is AuthScreenIntent.SetPasswordValidation -> setPasswordValidation(intent.validation)
      is AuthScreenIntent.AuthButtonClicked -> processAuthorizedButtonClick()
      is AuthScreenIntent.ChooseLocalMode -> signInLocally()
    }
  }

  private fun openSignInScreen() =
    _state.update { it.copy(action = AuthAction.SIGN_IN) }

  private fun openSignUpScreen() =
    _state.update { it.copy(action = AuthAction.SIGN_UP) }

  private fun openPasswordResetScreen() =
    _state.update { it.copy(action = AuthAction.RESET_PASSWORD) }

  private fun setEmail(email: String) =
    _state.update { it.copy(email = email, isEmailValid = isEmail(email)) }

  private fun setPassword(password: String) {
    _state.update {
      it.copy(
        password = password,
        passwordRating = validatePassword(password, it.passwordValidation)
      )
    }
  }

  private fun setPasswordValidation(validation: String) {
    _state.update {
      it.copy(
        password = it.password,
        passwordRating = validatePassword(it.password, validation)
      )
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
    setLoadingState(isLoading = true)
    signUpUseCase(email, password)
      .onSuccess { sendMessage(R.string.common_auth_screen_sign_up_success) }
      .onFailure { e -> openErrorDialog(error = e) }
    setLoadingState(isLoading = false)
  }

  private suspend fun signIn(email: String, password: String) {
    setLoadingState(isLoading = true)
    signInUseCase.invoke(email, password)
      .onSuccess {
        sendMessage(R.string.common_auth_screen_sign_in_success)
        _effect.emit(AuthScreenEffect.OpenHomeScreen)
      }
      .onFailure { e -> openErrorDialog(error = e) }
    setLoadingState(isLoading = false)
  }

  private suspend fun signInLocally() {
    // TODO
  }

  private fun setLoadingState(isLoading: Boolean) {
    _state.update { it.copy(isLoading = isLoading) }
  }

  private suspend fun openErrorDialog(error: Throwable?) {
    _effect.emit(AuthScreenEffect.OpenErrorDialog(error = error))
  }

  private suspend fun sendMessage(messageCode: Int) {
    _effect.emit(AuthScreenEffect.ShowMessage(messageCode))
  }
}