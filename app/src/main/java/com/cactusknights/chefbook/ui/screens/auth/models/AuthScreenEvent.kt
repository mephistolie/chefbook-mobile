package com.cactusknights.chefbook.ui.screens.auth.models

sealed class AuthScreenEvent {
    object OpenSignInScreen : AuthScreenEvent()
    object OpenSignUpScreen : AuthScreenEvent()
    object OpenPasswordResetScreen : AuthScreenEvent()
    object  CloseDialog : AuthScreenEvent()
    data class SignIn(val email: String, val password: String) : AuthScreenEvent()
    data class SignUp(val email: String, val password: String, val passwordValidation: String) : AuthScreenEvent()
    data class ResetPassword(val email: String) : AuthScreenEvent()
    object ChooseLocalMode : AuthScreenEvent()
}