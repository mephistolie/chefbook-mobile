package com.cactusknights.chefbook.core.ui

import androidx.compose.runtime.Composable
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState
import com.mysty.chefbook.core.ui.compose.providers.ContentAccessProvider
import com.mysty.chefbook.core.ui.compose.providers.ContentType

@Composable
fun RecipeEncryptionProvider(
    encryption: EncryptionState,
    content: @Composable () -> Unit,
) =
    ContentAccessProvider(
        contentType = when (encryption) {
            is EncryptionState.Standard -> ContentType.DECRYPTED
            is EncryptionState.Decrypted -> ContentType.DECRYPTABLE
            is EncryptionState.Encrypted -> ContentType.ENCRYPTED
        },
        content = content,
    )
