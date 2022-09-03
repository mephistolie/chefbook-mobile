package com.cactusknights.chefbook.ui.screens.recipebook.views.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.gridItems
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.CategoryCard
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.CategoryCardSkeleton
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.NewCategoryCard
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.modifiers.simpleClickable
import kotlin.math.min

fun LazyListScope.categoriesBlock(
    categories: List<Category>?,
    isCategoriesExpanded: Boolean,
    onCategoryClicked: (Int) -> Unit,
    onNewCategoryClicked: () -> Unit,
    onExpandClicked: () -> Unit
) {

    item {
        Text(
            text = stringResource(id = R.string.common_general_categories),
            style = ChefBookTheme.typography.h3,
            color = ChefBookTheme.colors.foregroundPrimary,
            modifier = Modifier.padding(12.dp, 24.dp, 12.dp, 12.dp),
        )
    }
    if (categories != null) {
        gridItems(
            size = if (!isCategoriesExpanded) min(categories.size + 1, 4) else categories.size + 1,
            columnCount = 4,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp, 0.dp),
            animated = true,
        ) { index ->
            if (index < categories.size) {
                CategoryCard(categories[index]) { onCategoryClicked(it.id) }
            } else {
                NewCategoryCard(onClicked = onNewCategoryClicked)
            }
        }
        item {
            if (categories.size > 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .simpleClickable(onClick = onExpandClicked)
                        .padding(12.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        text = if (!isCategoriesExpanded) stringResource(id = R.string.common_general_more) else stringResource(
                            id = R.string.common_general_less
                        ),
                        style = ChefBookTheme.typography.headline1,
                        color = ChefBookTheme.colors.foregroundSecondary,
                    )
                    Icon(
                        imageVector =
                        if (!isCategoriesExpanded)
                            ImageVector.vectorResource(R.drawable.ic_arrow_down)
                        else
                            ImageVector.vectorResource(R.drawable.ic_arrow_up),
                        tint = ChefBookTheme.colors.foregroundSecondary,
                        modifier = Modifier
                            .size(14.dp),
                        contentDescription = null,
                    )
                }
            }
        }
    } else {
        gridItems(
            size = 4,
            columnCount = 4,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp, 0.dp)
        ) {
            CategoryCardSkeleton()
        }
    }
}
