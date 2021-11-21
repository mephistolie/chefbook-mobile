package com.cactusknights.chefbook.screens.auth

enum class SignStates {
    SIGN_IN, SIGN_UP, RESTORE_PASSWORD, SIGNED_IN
}

class AuthActivityState (
    val authState: SignStates = SignStates.SIGN_IN,
    val inProgress: Boolean = false,
    val message: Any? = null
)