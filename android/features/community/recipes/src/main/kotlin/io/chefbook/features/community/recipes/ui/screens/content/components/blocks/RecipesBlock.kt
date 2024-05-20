package io.chefbook.features.community.recipes.ui.screens.content.components.blocks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.features.community.recipes.ui.screens.content.components.elements.NothingFoundBanner
import io.chefbook.features.community.recipes.ui.screens.content.components.elements.RecipeCard
import io.chefbook.features.community.recipes.ui.screens.content.components.elements.RecipeCardSkeleton

const val RECIPE_CARD_KEY_PREFIX = "recipe_card"

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyGridScope.recipesBlock(
  recipes: List<DecryptedRecipeInfo>,
  isLoading: Boolean,
  offerCreateRecipe: Boolean,
  onScrollEnded: () -> Unit,
  onRecipeClicked: (String) -> Unit,
  onCreateRecipeClick: () -> Unit,
) {
  items(
    count = recipes.size,
    key = { index -> "${RECIPE_CARD_KEY_PREFIX}_${recipes[index].id}" },
  ) { index ->
    if (index == recipes.lastIndex) onScrollEnded()

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
  when {
    isLoading -> items(if (recipes.size % 2 == 1) 5 else 4) { index ->
      val isSecondColumn = (recipes.size + index) % 2 == 1
      RecipeCardSkeleton(
        modifier = Modifier
          .padding(
            start = if (isSecondColumn) 6.dp else 12.dp,
            end = if (isSecondColumn) 12.dp else 6.dp,
            bottom = 16.dp,
          )
          .animateItemPlacement()
      )
    }
    recipes.isEmpty() -> item(span = { GridItemSpan(2) }) {
      NothingFoundBanner(
        offerCreateRecipe = offerCreateRecipe,
        onCreateRecipeClick = onCreateRecipeClick
      )
    }
  }
}
