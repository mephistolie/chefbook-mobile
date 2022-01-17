package com.cactusknights.chefbook.screens.auth.models

sealed class AuthScreenState(val isLoading : Boolean = false) {
    class SignInScreen(isLoading: Boolean = false) : AuthScreenState(isLoading)
    class SignUpScreen(isLoading: Boolean = false) : AuthScreenState(isLoading)
    class RestorePasswordScreen(isLoading: Boolean = false) : AuthScreenState(isLoading)
}