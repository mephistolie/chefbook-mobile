//package io.chefbook.features.auth.password.ui
//
//import io.chefbook.features.auth.Res
//import io.chefbook.features.auth.password.ui.mvi.PasswordFormIntent
//import io.chefbook.features.auth.password.ui.mvi.PasswordFormSideEffect
//import io.chefbook.features.auth.password.ui.mvi.PasswordFormState
//import io.chefbook.libs.exceptions.ServerException
//import io.chefbook.libs.mvi.BaseStore
//import io.chefbook.libs.utils.auth.isEmail
//import io.chefbook.libs.utils.auth.isNickname
//import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update
//
//private typealias State = PasswordFormState
//private typealias SideEffect = PasswordFormSideEffect
//private typealias Intent = PasswordFormIntent
//
//class PasswordFormStore(
//  login: String,
//  private val signInUseCase: SignInUseCase,
//) : BaseStore<State, SideEffect, Intent>() {
//
//  override val stateFlow: StateFlow<State> field = MutableStateFlow(
//    State(
//      login = login,
//    )
//  )
//
//  override val sideEffectsFlow: Flow<SideEffect> field = MutableSharedFlow()
//
//  override suspend fun reduce(intent: Intent) {
//    when (intent) {
//      is PasswordFormIntent.PasswordEntered -> reducePasswordEntered(intent.password)
//      PasswordFormIntent.SignInButtonClicked -> TODO()
//    }
//  }
//
//  private fun reducePasswordEntered(password: String) {
//    stateFlow.update { state ->
//      state.copy(
//        password = password.trim(),
//        isSignInButtonEnabled = password.isNotBlank(),
//      )
//    }
//  }
//
//  private suspend fun reduceSignInButtonClicked() {
//    val state = stateFlow.value
//
//    stateFlow.update { state -> state.copy(isSignInButtonLoading = true) }
//    val result = signInUseCase.invoke(state.login, state.password)
//    val e = result.exceptionOrNull() ?: return
//
//    stateFlow.update { state ->
//      state.copy(
//        password = "",
//        isSignInButtonEnabled = false,
//        isSignInButtonLoading = false,
//      )
//    }
//
//    if (e is ServerException) {
//      when {
//        e.type == ServerException.INVALID_CREDENTIALS -> {
//          showToast(io.chefbook.core.android.R.string.common_general_server_error_invalid_credentials)
//        }
//
//        e.type == ServerException.PROFILE_NOT_ACTIVATED -> {
//          showToast(io.chefbook.core.android.R.string.common_general_server_error_profile_not_activated)
//          return@onFailure _state.emit(getSignInState())
//        }
//
//        e.type == ServerException.PROFILE_BLOCKED -> {
//          this.login = ""
//          showToast(CoreR.string.common_general_server_error_profile_blocked)
//          return@onFailure _state.emit(getSignInState())
//        }
//
//        e.isServerSide -> {
//          showToast(CoreR.string.common_general_server_error)
//        }
//      }
//    }
//  }
//
//  private suspend fun reduceSignedInProfilesButtonClicked() {
////    sideEffectsFlow.emit(LoginFormSideEffect.SignedInProfilesButtonClicked)
//  }
//
//  private fun isLoginValid(login: String): Boolean = isNickname(login) || isEmail(login)
//}
