package io.chefbook.ui.common.providers

import androidx.compose.runtime.Composable
import io.chefbook.core.android.compose.providers.ContentAccessProvider
import io.chefbook.core.android.compose.providers.ContentType

@Composable
fun RecipeEncryptionProvider(
  isEncryptionEnabled: Boolean,
  isDecrypted: Boolean = true,
  content: @Composable () -> Unit,
) =
  ContentAccessProvider(
    contentType = when {
      !isEncryptionEnabled -> ContentType.DECRYPTED
      isDecrypted -> ContentType.DECRYPTABLE
      else -> ContentType.ENCRYPTED
    },
    content = content,
  )
