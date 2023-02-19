package com.mysty.chefbook.features.recipe.input.ui.dialogs.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.radibuttons.RadioButton

@Composable
fun RadioElement(
    icon: ImageVector,
    name: String,
    description: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Row(
        modifier = Modifier
            .padding(top = 18.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .simpleClickable(onClick = onSelected),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .padding(end = 12.dp)
                .weight(1F)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                tint = colors.foregroundPrimary,
                modifier = Modifier
                    .size(24.dp),
                contentDescription = null,
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = name,
                    style = typography.subhead2,
                    color = colors.foregroundSecondary,
                )
                Text(
                    text = description,
                    style = typography.caption1,
                    color = colors.foregroundPrimary,
                )
            }
        }
        RadioButton(
            isSelected = isSelected,
            onClick = {},
            isEnabled = false,
        )
    }
}