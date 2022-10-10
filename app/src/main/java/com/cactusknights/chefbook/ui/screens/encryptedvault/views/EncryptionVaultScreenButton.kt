package com.cactusknights.chefbook.ui.screens.encryptedvault.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun EncryptionVaultScreenButton(
    iconId: Int,
    textId: Int,
    onClick: () -> Unit,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconId),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 32.dp)
                .size(24.dp)
        )
        Text(
            text = stringResource(textId),
            color = colors.foregroundPrimary,
            textAlign = TextAlign.Center,
            style = typography.body1,
        )
    }
}
