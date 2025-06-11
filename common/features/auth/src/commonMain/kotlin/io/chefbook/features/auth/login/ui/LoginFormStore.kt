//package io.chefbook.features.auth.login.ui
//
//import io.chefbook.features.auth.login.ui.mvi.LoginFormIntent
//import io.chefbook.features.auth.login.ui.mvi.LoginFormSideEffect
//import io.chefbook.features.auth.login.ui.mvi.LoginFormState
//import io.chefbook.libs.mvi.BaseStore
//import io.chefbook.libs.utils.auth.isEmail
//import io.chefbook.libs.utils.auth.isNickname
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update
//
//private typealias State = LoginFormState
//private typealias SideEffect = LoginFormSideEffect
//private typealias Intent = LoginFormIntent
//
//class LoginFormStore(
//  initialLogin: String,
//) : BaseStore<State, SideEffect, Intent>() {
//
//  override val stateFlow: StateFlow<State> field = MutableStateFlow(
//    State(
//      login = initialLogin,
//      isSignInButtonEnabled = isLoginValid(initialLogin),
//      isProfilesListButtonVisible = false,
//    )
//  )
//
//  override val sideEffectsFlow: Flow<SideEffect> field = MutableSharedFlow()
//
//  override suspend fun reduce(intent: Intent) {
//    when (intent) {
//      is LoginFormIntent.LoginFormEntered -> reduceLoginEntered(intent.login)
//      LoginFormIntent.LoginFormButtonClicked -> reduceLoginButtonClicked()
//      LoginFormIntent.SignedInProfilesButtonClicked -> TODO()
//    }
//  }
//
//  private fun reduceLoginEntered(login: String) {
//    val formattedLogin = login.trim()
//    stateFlow.update { state ->
//      state.copy(
//        login = formattedLogin,
//        isSignInButtonEnabled = isLoginValid(formattedLogin),
//      )
//    }
//  }
//
//  private suspend fun reduceLoginButtonClicked() {
//    sideEffectsFlow.emit(LoginFormSideEffect.LoginFormButtonClicked(login = stateFlow.value.login))
//  }
//
//  private suspend fun reduceSignedInProfilesButtonClicked() {
//    sideEffectsFlow.emit(LoginFormSideEffect.SignedInProfilesButtonClicked)
//  }
//
//  private fun isLoginValid(login: String): Boolean = isNickname(login) || isEmail(login)
//}
