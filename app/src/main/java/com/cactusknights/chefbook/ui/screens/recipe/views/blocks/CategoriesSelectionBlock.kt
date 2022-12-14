package com.cactusknights.chefbook.ui.screens.recipe.views.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.category.Category
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun CategoriesSelectionBlock(
    categories: List<Category>,
    initialSelectedCategories: List<String>,
    onDiscard: () -> Unit,
    onConfirm: (List<String>) -> Unit,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val selectedCategoriesIds = remember { initialSelectedCategories.toMutableStateList() }

    Column {
        Text(
            text = stringResource(R.string.common_recipe_screen_choose_categories),
            style = typography.headline1,
            color = colors.foregroundSecondary,
            modifier = Modifier.padding(top = 12.dp)
        )
        FlowRow(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 12.dp),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
        ) {
            for (category in categories) {
                DynamicButton(
                    text = "${category.cover.orEmpty()} ${category.name}".trim(),
                    onClick = {
                        if (category.id !in selectedCategoriesIds)
                            selectedCategoriesIds.add(category.id)
                        else
                            selectedCategoriesIds.remove(category.id)
                    },
                    modifier = Modifier.height(38.dp),
                    horizontalPadding = 8.dp,
                    selectedBackground = colors.foregroundPrimary,
                    isSelected = category.id in selectedCategoriesIds,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DynamicButton(
                leftIcon = ImageVector.vectorResource(R.drawable.ic_cross),
                isSelected = false,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
                    .height(44.dp),
                onClick = onDiscard,
                unselectedForeground = colors.foregroundPrimary,
            )
            DynamicButton(
                leftIcon = ImageVector.vectorResource(R.drawable.ic_check),
                isSelected = true,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
                    .height(44.dp),
                onClick = { onConfirm(selectedCategoriesIds) },
            )
        }
        Divider(
            color = colors.backgroundSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(1.dp)
        )
    }
}
