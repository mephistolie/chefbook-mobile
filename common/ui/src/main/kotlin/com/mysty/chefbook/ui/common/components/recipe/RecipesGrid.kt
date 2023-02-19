package com.mysty.chefbook.ui.common.components.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo

@Composable
fun RecipesGrid(
    recipes: List<RecipeInfo>,
    onRecipeClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(recipes) { index, recipe ->
            val modifier = if (index < 2) Modifier.padding(top = 12.dp) else Modifier
            RecipeCard(recipe = recipe, modifier = modifier, onRecipeClick = { onRecipeClick(it.id) })
        }
    }
}
