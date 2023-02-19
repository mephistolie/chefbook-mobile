package com.mysty.chefbook.ui.common.providers

import androidx.compose.runtime.Composable
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.core.android.compose.providers.ContentAccessProvider
import com.mysty.chefbook.core.android.compose.providers.ContentType

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
