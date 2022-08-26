package com.cactusknights.chefbook.ui.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OneButtonDialog(
    title: String,
    onHide: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClick: () -> Unit = onHide,
    buttonIcon: ImageVector = ImageVector.vectorResource(R.drawable.ic_check),
    buttonText: String = stringResource(R.string.common_general_ok).uppercase(),
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Dialog(
        onDismissRequest = onHide,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp)
                .background(
                    color = colors.backgroundPrimary,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp, 24.dp, 24.dp, 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = typography.h3,
                color = colors.foregroundPrimary
            )
            description?.let {
                Text(
                    text = description,
                    style = typography.body1,
                    color = colors.foregroundSecondary,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            DynamicButton(
                leftIcon = buttonIcon,
                text = buttonText,
                isSelected = true,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .height(44.dp)
                    .requiredWidthIn(144.dp),
                onClick = onClick,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightOneButtonDialog() {
    ThemedOneButtonDialog(false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkOneButtonDialog() {
    ThemedOneButtonDialog(true)
}

@Composable
private fun ThemedOneButtonDialog(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            OneButtonDialog(
                title = stringResource(R.string.common_general_unknown_error),
                description = stringResource(R.string.common_general_unknown_error),
                onHide = {},
            )
        }
    }
}