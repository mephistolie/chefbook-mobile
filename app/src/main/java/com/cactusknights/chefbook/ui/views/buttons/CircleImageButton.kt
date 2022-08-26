package com.cactusknights.chefbook.ui.views.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun CircleImageButton(
    image: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 0.dp,
    tint: Color = ChefBookTheme.colors.foregroundPrimary,
    background: Color = ChefBookTheme.colors.backgroundTertiary,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(percent = 100),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background,
        ),
        elevation = null,
        contentPadding = PaddingValues(contentPadding),
        modifier = modifier.aspectRatio(1F)
    ) {
        Icon(
            imageVector = image,
            tint = tint,
            contentDescription = stringResource(id = R.string.common_general_email),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLightCircularImageButton() {
    ThemedInputFields(false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkCircularImageButton() {
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
                CircleImageButton(
                    image = Icons.Outlined.Lock,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}