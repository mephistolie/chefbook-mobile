package com.cactusknights.chefbook.screens.auth.models

sealed class AuthViewEffect {
    data class Message(val messageId: Int) : AuthViewEffect()
    object SignedIn : AuthViewEffect()
}