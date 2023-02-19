package com.mysty.chefbook.navigation.styles

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.DialogProperties
import com.ramcosta.composedestinations.spec.DestinationStyle

object DismissibleDialog : DestinationStyle.Dialog {
    @OptIn(ExperimentalComposeUiApi::class)
    override val properties = DialogProperties(
        usePlatformDefaultWidth = false,
    )
}

object NonDismissibleDialog : DestinationStyle.Dialog {
    @OptIn(ExperimentalComposeUiApi::class)
    override val properties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        usePlatformDefaultWidth = false,
    )
}
