package com.cactusknights.chefbook.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState

@Composable
fun RecipeEncryptionProvider(
    encryption: EncryptionState,
    content: @Composable () -> Unit,
) {
    val dataType = when(encryption) {
        is EncryptionState.Standard -> DataType.DECRYPTED
        is EncryptionState.Decrypted -> DataType.DECRYPTABLE
        is EncryptionState.Encrypted -> DataType.ENCRYPTED
    }
    CompositionLocalProvider(
        LocalDataAccess provides dataType,
        content = content
    )
}

@Composable
fun DataAccessProvider(
    isEncrypted: Boolean,
    content: @Composable () -> Unit,
) {
    val dataType = if (isEncrypted) DataType.ENCRYPTED else DataType.DECRYPTED
    CompositionLocalProvider(
        LocalDataAccess provides dataType,
        content = content
    )
}

@Composable
fun DataAccessProvider(
    type: DataType,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDataAccess provides type,
        content = content
    )
}

object DataAccess {
    val type: DataType
        @Composable
        get() = LocalDataAccess.current
}

private val LocalDataAccess = staticCompositionLocalOf {
    DataType.DECRYPTED
}

enum class DataType {
    DECRYPTED, DECRYPTABLE, ENCRYPTED
}
