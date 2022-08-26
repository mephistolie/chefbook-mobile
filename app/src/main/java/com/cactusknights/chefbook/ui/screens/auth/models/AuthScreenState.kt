package com.cactusknights.chefbook.ui.screens.auth.models

data class AuthScreenState(
    val action: AuthAction = AuthAction.SIGN_IN,
    val progress: AuthProgress = AuthProgress.INPUT,
    val error: Throwable? = null
)

enum class AuthAction {
    SIGN_IN, SIGN_UP, RESET_PASSWORD
}

enum class AuthProgress {
    INPUT, LOADING, ERROR
}