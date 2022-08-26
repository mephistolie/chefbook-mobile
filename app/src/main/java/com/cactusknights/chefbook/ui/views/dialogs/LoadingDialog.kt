package com.cactusknights.chefbook.ui.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadingDialog() {
    val colors = ChefBookTheme.colors

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
                .background(
                    color = colors.backgroundPrimary,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                color = colors.tintPrimary,
                modifier = Modifier
                    .size(48.dp)
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
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            LoadingDialog()
        }
    }
}