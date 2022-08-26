package com.cactusknights.chefbook.ui.screens.recipebook.views.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.gridItems
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.RecipeCard
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.RecipeCardSkeleton
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

fun LazyListScope.allRecipesBlock(
    recipes: List<RecipeInfo>?,
    categories: List<Category>?,
    onRecipeClicked: (Int) -> Unit,
) {

    item {
        val topPadding =
            if (categories == null || categories.size < 4) 16.dp else 0.dp
        Text(
            text = stringResource(id = R.string.common_recipe_book_screen_all_recipes),
            style = ChefBookTheme.typography.h3,
            color = ChefBookTheme.colors.foregroundPrimary,
            modifier = Modifier.padding(12.dp, topPadding, 12.dp, 12.dp),
        )
    }
    if (recipes != null) {
        gridItems(
            data = recipes,
            columnCount = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 16.dp)
        ) { itemData ->
            RecipeCard(itemData) { onRecipeClicked(it.id) }
        }
    } else {
        gridItems(
            size = 2,
            columnCount = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp, 0.dp)
        ) {
            RecipeCardSkeleton()
        }
    }
    item {
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
