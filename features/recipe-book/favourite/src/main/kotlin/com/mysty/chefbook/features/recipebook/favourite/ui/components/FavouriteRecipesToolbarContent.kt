package com.mysty.chefbook.features.recipebook.favourite.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.recipebook.favourite.R

@Composable
internal fun FavouriteRecipesToolbarContent() {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_favourite),
            tint = colors.foregroundPrimary,
            modifier = Modifier
                .size(18.dp)
                .aspectRatio(1F),
            contentDescription = null,
        )
        Text(
            text = stringResource(R.string.common_general_favourite),
            modifier = Modifier.padding(start = 4.dp),
            style = typography.h4,
            color = colors.foregroundPrimary,
        )
    }
}
