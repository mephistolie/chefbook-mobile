package com.cactusknights.chefbook.screens.common.encryption.models

sealed class EncryptionScreenViewEffect {
    data class Message(val messageId: Int) : EncryptionScreenViewEffect()
    object Done : EncryptionScreenViewEffect()
}