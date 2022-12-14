package com.mysty.chefbook.design.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.dimens.BigIconSize
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape16

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadingDialog() {
    val colors = LocalTheme.colors

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(color = colors.backgroundPrimary, shape = RoundedCornerShape16)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                color = colors.tintPrimary,
                modifier = Modifier
                    .size(BigIconSize)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightLoadingDialog() {
    ThemedLoadingDialog(false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkLoadingDialog() {
    ThemedLoadingDialog(true)
}

@Composable
private fun ThemedLoadingDialog(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = LocalTheme.colors.backgroundPrimary
        ) {
            LoadingDialog()
        }
    }
}