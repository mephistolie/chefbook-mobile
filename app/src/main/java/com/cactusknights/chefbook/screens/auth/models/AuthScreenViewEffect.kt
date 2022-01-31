package com.cactusknights.chefbook.screens.auth.models

sealed class AuthScreenViewEffect {
    data class Message(val messageId: Int) : AuthScreenViewEffect()
    object SignedIn : AuthScreenViewEffect()
}