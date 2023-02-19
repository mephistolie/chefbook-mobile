package com.mysty.chefbook.features.auth.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class AuthScreenIntent : MviIntent {
    object OpenSignInScreen : AuthScreenIntent()
    object OpenSignUpScreen : AuthScreenIntent()
    object OpenPasswordResetScreen : AuthScreenIntent()
    data class SetEmail(val email: String) : AuthScreenIntent()
    data class SetPassword(val password: String) : AuthScreenIntent()
    data class SetPasswordValidation(val validation: String) : AuthScreenIntent()
    object AuthButtonClicked : AuthScreenIntent()
    object ChooseLocalMode : AuthScreenIntent()
}