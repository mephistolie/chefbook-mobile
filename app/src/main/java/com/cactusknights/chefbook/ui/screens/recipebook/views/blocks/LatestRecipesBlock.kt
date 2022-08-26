package com.cactusknights.chefbook.ui.screens.recipebook.views.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.LatestRecipeCard
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.LatestRecipeCardSkeleton
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

fun LazyListScope.latestRecipesBlock(
    recipes: List<RecipeInfo>?,
    onRecipeClicked: (Int) -> Unit,
) {
    if (recipes == null || recipes.isNotEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.common_recipe_book_screen_quick_access),
                style = ChefBookTheme.typography.h3,
                color = ChefBookTheme.colors.foregroundPrimary,
                modifier = Modifier.padding(12.dp, 24.dp, 12.dp, 12.dp),
            )
        }
        if (recipes != null) {
            item {
                recipes.let { recipes ->
                    LazyRow {
                        item {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        items(recipes) { recipe ->
                            LatestRecipeCard(
                                recipe = recipe,
                                onRecipeClicked = { onRecipeClicked(it.id) }
                            )
                        }
                    }
                }
            }
        } else {
            item {
                LazyRow {
                    item {
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    items(2) {
                        LatestRecipeCardSkeleton()
                    }
                }
            }
        }
    }
}
