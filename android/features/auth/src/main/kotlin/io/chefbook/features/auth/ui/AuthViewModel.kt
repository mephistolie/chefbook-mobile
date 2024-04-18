package io.chefbook.features.auth.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import io.chefbook.features.auth.R
import io.chefbook.features.auth.data.oauth.GoogleAuthenticator
import io.chefbook.features.auth.ui.mvi.AuthScreenEffect
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.libs.exceptions.ServerException
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.utils.auth.PasswordRating
import io.chefbook.libs.utils.auth.isEmail
import io.chefbook.libs.utils.auth.isNickname
import io.chefbook.libs.utils.auth.validatePassword
import io.chefbook.sdk.auth.api.external.domain.usecases.ActivateProfileUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveProfileDeletionUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.RequestPasswordResetUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ResetPasswordUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.RestoreProfileUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInGoogleUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import io.chefbook.core.android.R as coreR

internal typealias IAuthViewModel = MviViewModel<AuthScreenState, AuthScreenIntent, AuthScreenEffect>

internal class AuthViewModel(
  private var userId: String = "",
  private var activationCode: String = "",
  private var passwordResetCode: String = "",
  context: Context,
  private val signUpUseCase: SignUpUseCase,
  private val signInUseCase: SignInUseCase,
  private val signInGoogleUseCase: SignInGoogleUseCase,
  private val activateProfileUseCase: ActivateProfileUseCase,
  private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
  private val resetPasswordUseCase: ResetPasswordUseCase,
  private val observeProfileDeletionUseCase: ObserveProfileDeletionUseCase,
  private val restoreProfileUseCase: RestoreProfileUseCase,
  private val signOutUseCase: SignOutUseCase,
  private val googleAuthenticator: GoogleAuthenticator,
) : BaseMviViewModel<AuthScreenState, AuthScreenIntent, AuthScreenEffect>() {

  private val resources = context.resources

  private var login = ""
  private var password = ""
  private var passwordValidation = ""

  override val _state = MutableStateFlow(
    when {
      userId.isNotBlank() && activationCode.isNotBlank() -> getProfileActivationState()
      userId.isNotBlank() && passwordResetCode.isNotBlank() -> getPasswordResetConfirmationState()
      else -> getSignInState()
    }
  )

  init {
    viewModelScope.launch {
      launch { googleAuthenticator.clearCredentialState() }
      if (userId.isNotBlank() && activationCode.isNotBlank()) activateProfile()
      observeProfileDeletion()
    }
  }

  private suspend fun observeProfileDeletion() {
    observeProfileDeletionUseCase()
      .collectInViewModelScope { deletionTimestamp ->
        when {
          deletionTimestamp != null ->
            _state.emit(AuthScreenState.ProfileRestoration(deletionTimestamp))

          _state.value is AuthScreenState.ProfileRestoration -> _state.update { getSignInState() }
        }
      }
  }

  override suspend fun reduceIntent(intent: AuthScreenIntent) {
    when (intent) {
      is AuthScreenIntent.SetLogin -> setLogin(intent.login)
      is AuthScreenIntent.SetPassword -> setPassword(intent.password)
      is AuthScreenIntent.SetPasswordValidation -> setPasswordValidation(intent.validation)
      is AuthScreenIntent.SetActivationCode -> setActivationCode(intent.code)

      is AuthScreenIntent.OpenSignUpForm -> _state.update { getSignUpState() }
      is AuthScreenIntent.OpenSignUpPasswordForm -> _state.update { getSignUpPasswordState() }
      is AuthScreenIntent.SignUp -> signUp()

      is AuthScreenIntent.OpenSignInForm -> _state.update { getSignInState() }
      is AuthScreenIntent.OpenSignInPasswordForm -> {
        this.password = ""
        this.passwordValidation = ""
        _state.update { getSignInPasswordState() }
      }

      is AuthScreenIntent.SignIn -> signIn()

      is AuthScreenIntent.SignInGoogleClicked -> signInGoogle(intent.context)
      is AuthScreenIntent.ChooseLocalMode -> signInLocally()

      is AuthScreenIntent.RequestPasswordReset -> requestPasswordReset()
      is AuthScreenIntent.ConfirmPasswordReset -> confirmPasswordReset()

      is AuthScreenIntent.RestoreProfile -> restoreProfile()
      is AuthScreenIntent.OpenSignOutConfirmationScreen -> _effect.emit(AuthScreenEffect.SignOutConfirmationScreenOpened)
      is AuthScreenIntent.SignOut -> signOutUseCase()
    }
  }

  private fun setLogin(login: String) {
    this.login = login
    actualizeState()
  }

  private fun setPassword(password: String) {
    if (password.length <= MAX_PASSWORD_LENGTH) {
      this.password = password
      actualizeState()
    }
  }

  private fun setPasswordValidation(validation: String) {
    this.passwordValidation = validation
    actualizeState()
  }

  private suspend fun setActivationCode(activationCode: String) {
    if (activationCode.length <= AuthScreenState.ProfileActivation.CODE_LENGTH) {
      this.activationCode = activationCode
      actualizeState()
    }
    if (activationCode.length == AuthScreenState.ProfileActivation.CODE_LENGTH) activateProfile()
  }

  private suspend fun signUp() {
    val login = this.login
    val password = this.password
    val passwordValidation = this.passwordValidation

    if (!isEmail(login)) return
    if (validatePassword(password, passwordValidation) != PasswordRating.VALID) return

    _state.emit(AuthScreenState.Loading)
    signUpUseCase(login, password)
      .also {
        this.password = ""
        this.passwordValidation = ""
      }
      .onSuccess { userIdToActivate ->
        if (userIdToActivate == null) {
          _state.emit(getSignInState())
        } else {
          userId = userIdToActivate
          _state.update { getProfileActivationState() }
        }
      }
      .onFailure { e ->
        if (e is ServerException) {
          when (e.type) {
            ServerException.PROFILE_EXISTS -> {
              showToast(coreR.string.common_general_server_error_profile_exists)
              return@onFailure _state.emit(getSignInState())
            }

            ServerException.PROFILE_BLOCKED -> {
              this.login = ""
              showToast(coreR.string.common_general_server_error_profile_blocked)
              return@onFailure _state.emit(getSignInState())
            }

            else -> e.message?.let { showToast(it) }
          }
        }
        _state.emit(getSignUpState())
      }
  }

  private suspend fun activateProfile() {
    if (userId.isEmpty()) return

    activateProfileUseCase(userId, activationCode)
      .also {
        this.activationCode = ""
      }
      .onSuccess {
        showToast(R.string.common_auth_screen_profile_activated)
        _state.emit(getSignInState())
      }
      .onFailure {
        _state.emit(getProfileActivationState())
      }
  }

  private suspend fun signIn() {
    val login = this.login
    val password = this.password

    if (!isNickname(login) && !isEmail(login)) return

    _state.emit(AuthScreenState.Loading)
    signInUseCase.invoke(login, password)
      .onSuccess { isSignedIn ->
        if (!isSignedIn) return
        _effect.emit(AuthScreenEffect.DashboardOpened)
      }
      .onFailure { e ->
        this.password = ""
        this.passwordValidation = ""
        if (e is ServerException) {
          when {
            e.type == ServerException.INVALID_CREDENTIALS -> {
              showToast(io.chefbook.core.android.R.string.common_general_server_error_invalid_credentials)
            }

            e.type == ServerException.PROFILE_NOT_ACTIVATED -> {
              showToast(io.chefbook.core.android.R.string.common_general_server_error_profile_not_activated)
              return@onFailure _state.emit(getSignInState())
            }

            e.type == ServerException.PROFILE_BLOCKED -> {
              this.login = ""
              showToast(coreR.string.common_general_server_error_profile_blocked)
              return@onFailure _state.emit(getSignInState())
            }

            e.isServerSide -> {
              showToast(coreR.string.common_general_server_error)
            }
          }
        }
        _state.emit(getSignInPasswordState())
      }
  }

  private suspend fun signInGoogle(activityContext: Context) {
    _state.emit(AuthScreenState.Loading)
    googleAuthenticator.signInGoogle(activityContext)
      .onSuccess { token ->
        signInGoogleUseCase.invoke(token)
          .onSuccess { isSignedIn ->
            if (!isSignedIn) return
            _effect.emit(AuthScreenEffect.DashboardOpened)
          }
          .onFailure {
            googleAuthenticator.clearCredentialState()
          }
      }
      .onFailure { e -> Logger.e(e, "Google sign in failed") }
    _state.emit(getSignInState())
  }

  private suspend fun requestPasswordReset() {
    _state.emit(AuthScreenState.Loading)
    requestPasswordResetUseCase.invoke(login)
      .onSuccess { _state.update { getPasswordResetState() } }
      .onFailure { _state.update { getSignInPasswordState() } }
  }

  private suspend fun confirmPasswordReset() {
    if (userId.isBlank() || passwordResetCode.isBlank()) return

    val password = this.password
    val passwordValidation = this.passwordValidation
    if (validatePassword(password, passwordValidation) != PasswordRating.VALID) return

    _state.emit(AuthScreenState.Loading)
    resetPasswordUseCase.invoke(userId = userId, code = passwordResetCode, newPassword = password)
      .onSuccess {
        showToast(R.string.common_auth_screen_password_reset)
        _state.update { getSignInState() }
      }
      .onFailure { _state.update { getPasswordResetConfirmationState() } }
  }

  private suspend fun signInLocally() {
    // TODO
  }

  private suspend fun restoreProfile() {
    restoreProfileUseCase()
      .onSuccess {
        _effect.emit(AuthScreenEffect.DashboardOpened)
      }
  }

  private suspend fun showToast(messageId: Int) =
    _effect.emit(AuthScreenEffect.ToastShown(resources.getString(messageId)))

  private suspend fun showToast(message: String) =
    _effect.emit(AuthScreenEffect.ToastShown(message))

  private fun actualizeState() {
    _state.update { state ->
      when (state) {
        is AuthScreenState.Loading -> state
        is AuthScreenState.ProfileRestoration -> state
        is AuthScreenState.SignIn -> getSignInState()
        is AuthScreenState.SignInPassword -> getSignInPasswordState()
        is AuthScreenState.PasswordReset -> getPasswordResetState()
        is AuthScreenState.PasswordResetConfirmation -> getPasswordResetConfirmationState()
        is AuthScreenState.SignUp -> getSignUpState()
        is AuthScreenState.SignUpPassword -> getSignUpPasswordState()
        is AuthScreenState.ProfileActivation -> getProfileActivationState()
      }
    }
  }

  private fun getSignInState() = AuthScreenState.SignIn(
    login = login,
    isAuthButtonEnabled = isNickname(login) || isEmail(login)
  )

  private fun getSignInPasswordState() = AuthScreenState.SignInPassword(
    login = login,
    password = password,
  )

  private fun getSignUpState() = AuthScreenState.SignUp(
    email = login,
    isAuthButtonEnabled = isEmail(login),
  )

  private fun getPasswordResetState() = AuthScreenState.PasswordReset(
    login = login,
  )

  private fun getPasswordResetConfirmationState() = AuthScreenState.PasswordResetConfirmation(
    login = login,
    password = password,
    passwordValidation = passwordValidation,
    isAuthButtonEnabled = validatePassword(password, passwordValidation) == PasswordRating.VALID
  )

  private fun getSignUpPasswordState() = AuthScreenState.SignUpPassword(
    email = login,
    password = password,
    passwordValidation = passwordValidation,
    isAuthButtonEnabled = validatePassword(password, passwordValidation) == PasswordRating.VALID
  )

  private fun getProfileActivationState() = AuthScreenState.ProfileActivation(
    email = login,
    code = activationCode,
  )

  companion object {
    const val MAX_PASSWORD_LENGTH = 64
  }
}
