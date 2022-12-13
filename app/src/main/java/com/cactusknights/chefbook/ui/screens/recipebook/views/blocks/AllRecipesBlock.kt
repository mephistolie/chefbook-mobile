package com.cactusknights.chefbook.ui.screens.recipebook.views.blocks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.RecipeCard
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.RecipeCardSkeleton
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

private const val KEY_PREFIX = "all_recipes_card"

@OptIn(ExperimentalFoundationApi::class)
fun LazyGridScope.allRecipesBlock(
    recipes: List<RecipeInfo>?,
    categories: List<Category>?,
    onRecipeClicked: (String) -> Unit,
) {
    item(
        span = { GridItemSpan(4) }
    ) {
        val topPadding =
            if (categories == null || categories.size < 4) 16.dp else 0.dp
        Text(
            text = stringResource(id = R.string.common_recipe_book_screen_all_recipes),
            style = ChefBookTheme.typography.h3,
            color = ChefBookTheme.colors.foregroundPrimary,
            modifier = Modifier
                .padding(12.dp, topPadding, 12.dp, 12.dp)
                .animateItemPlacement(),
        )
    }
    if (recipes != null) {
        items(
            count = recipes.size,
            key = { index -> "${KEY_PREFIX}_${recipes[index].id}" },
            span = { GridItemSpan(2) },
        ) { index ->
            val isSecondColumn = index % 2 == 1
            RecipeCard(
                recipe = recipes[index],
                modifier = Modifier
                    .padding(
                        start = if (isSecondColumn) 6.dp else 12.dp,
                        end = if (isSecondColumn) 12.dp else 6.dp,
                        bottom = 16.dp,
                    )
                    .animateItemPlacement()
            ) { onRecipeClicked(it.id) }
        }
    } else {
        items(4) {
            RecipeCardSkeleton()
        }
    }
    item {
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
