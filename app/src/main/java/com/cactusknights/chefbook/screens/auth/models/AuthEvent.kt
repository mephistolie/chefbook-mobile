package com.cactusknights.chefbook.screens.auth.models

sealed class AuthEvent {
    object SignInSelected : AuthEvent()
    object SignUpScreen : AuthEvent()
    object RestorePasswordScreen : AuthEvent()
    data class SignIn(val email: String, val password: String) : AuthEvent()
    data class SignUp(val email: String, val password: String, val passwordValidation: String) : AuthEvent()
    data class RestorePassword(val email: String) : AuthEvent()
    object SignInLocally : AuthEvent()
}