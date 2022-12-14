package com.cactusknights.chefbook.ui.screens.recipebook.views.blocks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.LatestRecipeCard
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.LatestRecipeCardSkeleton
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

private const val KEY_PREFIX = "quick_access_card"

@OptIn(ExperimentalFoundationApi::class)
fun LazyGridScope.quickAccessBlock(
    recipes: List<RecipeInfo>?,
    onRecipeClicked: (String) -> Unit,
) {
    if (recipes == null || recipes.isNotEmpty()) {
        item(
            span = { GridItemSpan(4) }
        ) {
            Text(
                text = stringResource(id = R.string.common_recipe_book_screen_quick_access),
                style = LocalTheme.typography.h3,
                color = LocalTheme.colors.foregroundPrimary,
                modifier = Modifier.padding(12.dp, 24.dp, 12.dp, 12.dp),
            )
        }
        item(
            span = { GridItemSpan(4) }
        ) {
            LazyRow {
                item {
                    Spacer(modifier = Modifier.width(12.dp))
                }
                recipes?.let {
                    items(
                        items = recipes,
                        key = { recipe -> "${KEY_PREFIX}_${recipe.id}" }
                    ) { recipe ->
                        LatestRecipeCard(
                            recipe = recipe,
                            onRecipeClicked = { onRecipeClicked(it.id) },
                            modifier = Modifier.animateItemPlacement(),
                        )
                    }
                } ?: items(2) {
                    LatestRecipeCardSkeleton()
                }
            }
        }
    }
}
