package com.cactusknights.chefbook.screens.auth

import com.cactusknights.chefbook.legacy.enums.SignStates

class AuthActivityState (
    val authState: SignStates = SignStates.SIGN_IN,
    val inProgress: Boolean = false
)