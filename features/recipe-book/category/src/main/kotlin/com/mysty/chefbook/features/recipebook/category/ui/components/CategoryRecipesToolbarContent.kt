package com.mysty.chefbook.features.recipebook.category.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.recipebook.category.R

@Composable
internal fun CategoryRecipesToolbarContent(
    category: Category?,
    recipesCount: Int,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${category?.cover.orEmpty()} ${category?.name}".trim(),
            style = typography.h4,
            color = colors.foregroundPrimary,
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
            tint = colors.foregroundSecondary,
            modifier = Modifier
                .padding(start = 2.dp, top = 1.dp)
                .size(12.dp)
                .aspectRatio(1F),
            contentDescription = null,
        )
    }
    Text(
        text = if (recipesCount > 0) {
            "${stringResource(R.string.common_general_recipes)}: $recipesCount"
        } else {
            stringResource(R.string.common_general_no_recipes)
        },
        maxLines = 1,
        style = typography.subhead1,
        color = colors.foregroundSecondary,
    )
}