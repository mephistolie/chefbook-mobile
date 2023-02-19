package com.mysty.chefbook.ui.common.dialogs.onebutton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.R
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.shapes.DialogShape

@Composable
fun OneButtonDialogContent(
    onClick: () -> Unit,
    title: String,
    description: String? = null,
    buttonIcon: ImageVector = ImageVector.vectorResource(R.drawable.ic_check),
    buttonText: String = stringResource(R.string.common_general_ok).uppercase(),
    isButtonSelected: Boolean = true,
    fullWidthButton: Boolean = false,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    var buttonModifier = Modifier
        .padding(top = 32.dp)
        .height(44.dp)
    buttonModifier = if (fullWidthButton) buttonModifier.fillMaxWidth() else buttonModifier.requiredWidthIn(144.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp)
            .background(
                color = colors.backgroundPrimary,
                shape = DialogShape,
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
            isSelected = isButtonSelected,
            modifier = buttonModifier,
        onClick = onClick,
        )
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
            color = LocalTheme.colors.backgroundPrimary
        ) {
            OneButtonDialogContent(
                title = stringResource(R.string.common_general_unknown_error),
                description = stringResource(R.string.common_general_unknown_error),
                onClick = {},
            )
        }
    }
}