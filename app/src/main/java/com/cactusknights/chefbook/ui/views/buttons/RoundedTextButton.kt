package com.cactusknights.chefbook.ui.views.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun RoundedTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color = ChefBookTheme.colors.foregroundPrimary,
    color: Color = ChefBookTheme.colors.backgroundTertiary,
) {
    val colors = ButtonDefaults.buttonColors(backgroundColor = color, contentColor = textColor)

    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        elevation = null,
        shape = RoundedCornerShape(percent = 100),
        colors = colors,
    ) {
        Text(
            text = text.uppercase(),
            style = ChefBookTheme.typography.body1,
            color = colors.contentColor(enabled = enabled).value,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLightSignInButton() {
    ThemedInputFields(false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkSignInButton() {
    ThemedInputFields(true)
}

@Composable
private fun ThemedInputFields(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            Column {
                RoundedTextButton(
                    text = stringResource(id = R.string.common_auth_screen_sign_in),
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}