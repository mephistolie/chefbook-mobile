package io.chefbook.navigation.styles

import androidx.compose.ui.window.DialogProperties
import com.ramcosta.composedestinations.spec.DestinationStyle

object DismissibleDialog : DestinationStyle.Dialog {
  override val properties = DialogProperties(
    usePlatformDefaultWidth = false,
  )
}

object NonDismissibleDialog : DestinationStyle.Dialog {
  override val properties = DialogProperties(
    dismissOnClickOutside = false,
    dismissOnBackPress = false,
    usePlatformDefaultWidth = false,
  )
}
