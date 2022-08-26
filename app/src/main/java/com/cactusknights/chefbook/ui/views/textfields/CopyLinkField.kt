package com.cactusknights.chefbook.ui.views.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.simpleClickable
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun CopyLinkField(
    link: String,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(44.dp),
) {

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Row(
        modifier = modifier
            .background(colors.backgroundTertiary, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = link,
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            style = typography.body1,
            maxLines = 1,
            color = colors.foregroundSecondary,
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_copy),
            tint = colors.foregroundPrimary,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1F)
                .simpleClickable(onCopy)
                .background(
                    color = colors.backgroundSecondary,
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                )
                .padding(10.dp),
            contentDescription = null,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLightCopyLinkField() {
    ThemedInputFields(false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkCopyLinkField() {
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
                CopyLinkField(
                    link = "https://chefbook.space/",
                    onCopy = {}
                )
            }
        }
    }
}