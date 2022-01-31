package com.cactusknights.chefbook.screens.common.encryption.models

sealed class EncryptionScreenState {
    object Disabled : EncryptionScreenState()
    object Locked : EncryptionScreenState()
    object Unlocking : EncryptionScreenState()
    object Unlocked : EncryptionScreenState()
}