package io.chefbook.features.auth.signup.password.mvi

import io.chefbook.libs.exceptions.ServerException
import io.chefbook.libs.mvi.BaseStore
import io.chefbook.libs.mvi.Store
import io.chefbook.libs.utils.auth.PasswordRating
import io.chefbook.libs.utils.auth.validatePassword
import io.chefbook.sdk.auth.api.external.domain.usecases.SignUpUseCase
import io.chefbook.ui.utils.Res as UtilsRes
import io.chefbook.ui.utils.commonGeneralServerErrorProfileBlocked
import io.chefbook.ui.utils.commonGeneralServerErrorProfileExists
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.getString

private typealias State = SignUpPasswordState
private typealias SideEffect = SignUpPasswordSideEffect
private typealias Intent = SignUpPasswordIntent

interface SignUpPasswordStore : Store<State, SideEffect, Intent>

class SignUpPasswordStoreImpl(
  email: String,
  private val signUpUseCase: SignUpUseCase,
) : BaseStore<State, SideEffect, Intent>(), SignUpPasswordStore {

  override val stateFlow: StateFlow<State> field = MutableStateFlow(
    State(
      email = email,
    )
  )

  override suspend fun reduce(intent: Intent) {
    when (intent) {
      SignUpPasswordIntent.Back -> reduceBack()
      is SignUpPasswordIntent.PasswordEntered -> reducePasswordEntered(intent.password)
      is SignUpPasswordIntent.PasswordValidationEntered -> reducePasswordValidationEntered(intent.passwordValidation)
      SignUpPasswordIntent.SignUpButtonClicked -> reduceSignUpButtonClicked()
    }
  }

  private suspend fun reduceBack() {
    sideEffectsFlow.emit(SignUpPasswordSideEffect.Back)
  }

  private fun reducePasswordEntered(password: String) {
    val formattedPassword = password.trim()
    stateFlow.update { state ->
      state.copy(
        password = formattedPassword,
        isSignUpButtonEnabled = validatePassword(
          password = password,
          repeatPassword = state.passwordValidation,
        ) == PasswordRating.VALID,
      )
    }
  }

  private fun reducePasswordValidationEntered(passwordValidation: String) {
    val formattedPasswordValidation = passwordValidation
    stateFlow.update { state ->
      state.copy(
        passwordValidation = formattedPasswordValidation,
        isSignUpButtonEnabled = validatePassword(
          password = formattedPasswordValidation,
          repeatPassword = state.passwordValidation,
        ) == PasswordRating.VALID,
      )
    }
  }

  private suspend fun reduceSignUpButtonClicked() {
    val state = stateFlow.value

    stateFlow.update { state -> state.copy(isSignUpButtonLoading = true) }
    val result = signUpUseCase.invoke(state.email, state.password)
    val e = result.exceptionOrNull() ?: return

    stateFlow.update { state ->
      state.copy(
        password = "",
        passwordValidation = "",
        isSignUpButtonEnabled = false,
        isSignUpButtonLoading = false,
      )
    }

    if (e is ServerException) {
      when (e.type) {
        ServerException.PROFILE_EXISTS -> {
          showToast(getString(UtilsRes.string.commonGeneralServerErrorProfileExists))
          sideEffectsFlow.emit(SignUpPasswordSideEffect.ProfileExists)
        }

        ServerException.PROFILE_BLOCKED -> {
          showToast(getString(UtilsRes.string.commonGeneralServerErrorProfileBlocked))
          sideEffectsFlow.emit(SignUpPasswordSideEffect.ProfileBlocked)
        }

        else -> e.message?.let { showToast(it) }
      }
    }
  }

  private suspend fun showToast(message: String) {
    sideEffectsFlow.emit(SignUpPasswordSideEffect.ToastShown(message))
  }
}
