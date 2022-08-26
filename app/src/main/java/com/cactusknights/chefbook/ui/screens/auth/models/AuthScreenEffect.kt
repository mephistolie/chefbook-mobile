package com.cactusknights.chefbook.ui.screens.auth.models

sealed class AuthScreenEffect {
    data class Message(val messageId: Int) : AuthScreenEffect()
    object SignedIn : AuthScreenEffect()
}