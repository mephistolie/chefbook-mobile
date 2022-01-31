package com.cactusknights.chefbook.screens.auth.models

sealed class AuthScreenEvent {
    object SignInSelected : AuthScreenEvent()
    object SignUpScreen : AuthScreenEvent()
    object RestorePasswordScreen : AuthScreenEvent()
    data class SignIn(val email: String, val password: String) : AuthScreenEvent()
    data class SignUp(val email: String, val password: String, val passwordValidation: String) : AuthScreenEvent()
    data class RestorePassword(val email: String) : AuthScreenEvent()
    object SignInLocally : AuthScreenEvent()
}