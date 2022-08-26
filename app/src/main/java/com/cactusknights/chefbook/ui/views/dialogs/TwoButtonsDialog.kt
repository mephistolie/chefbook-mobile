package com.cactusknights.chefbook.ui.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun TwoButtonsDialog(
    onHide: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.common_general_are_you_sure),
    description: String? = null,
    onLeftClick: () -> Unit = onHide,
    onRightClick: () -> Unit = onHide,
    leftButtonIcon: ImageVector = ImageVector.vectorResource(R.drawable.ic_cross),
    leftButtonText: String? = null,
    isLeftButtonPrimary: Boolean = false,
    rightButtonIcon: ImageVector = ImageVector.vectorResource(R.drawable.ic_check),
    rightButtonText: String? = null,
    isRightButtonPrimary: Boolean = true,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DynamicButton(
                    leftIcon = leftButtonIcon,
                    text = leftButtonText,
                    isSelected = isLeftButtonPrimary,
                    unselectedForeground = colors.foregroundPrimary,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                        .height(44.dp),
                    onClick = onLeftClick,
                )
                DynamicButton(
                    leftIcon = rightButtonIcon,
                    text = rightButtonText,
                    isSelected = isRightButtonPrimary,
                    unselectedForeground = colors.foregroundPrimary,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                        .height(44.dp),
                    onClick = onRightClick,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightTwoButtonsDialog() {
    ThemedTwoButtonsDialog(false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkTwoButtonsDialog() {
    ThemedTwoButtonsDialog(true)
}

@Composable
private fun ThemedTwoButtonsDialog(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            TwoButtonsDialog(
                onHide = {},
            )
        }
    }
}