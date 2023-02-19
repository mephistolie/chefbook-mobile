package com.mysty.chefbook.features.recipe.control.ui.components.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.toolbar.Toolbar
import com.mysty.chefbook.design.theme.dimens.SmallIconSize
import com.mysty.chefbook.features.recipe.control.R
import com.mysty.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenIntent

@Composable
internal fun RecipeControlScreenMenu(
  recipe: RecipeInfo,
  onIntent: (RecipeControlScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      leftButtonIconId = null,
      modifier = Modifier.padding(bottom = 4.dp)
    ) {
      Text(
        text = recipe.name,
        style = typography.h4,
        color = colors.foregroundPrimary,
        maxLines = 1,
      )
    }

    if (recipe.isSaved) {
      RecipeActionButton(
        onClick = { onIntent(RecipeControlScreenIntent.ChangeFavouriteStatus) },
        text = stringResource(
          if (recipe.isFavourite)
            R.string.common_recipe_control_screen_remove_from_favourite
          else
            R.string.common_recipe_control_screen_add_to_favourite
        ),
        iconId = if (recipe.isFavourite) R.drawable.ic_favourite else R.drawable.ic_unfavourite,
        isFirst = true,
      )
      RecipeActionButton(
        onClick = { onIntent(RecipeControlScreenIntent.ChangeCategories) },
        text = stringResource(R.string.common_recipe_control_screen_choose_categories),
        iconId = R.drawable.ic_arrow_right,
        iconSize = SmallIconSize,
        isLast = true,
      )
    }
    RecipeActionButton(
      onClick = {
        onIntent(
          if (recipe.isSaved)
            RecipeControlScreenIntent.OpenRemoveFromRecipeBookDialog
          else
            RecipeControlScreenIntent.ChangeSavedStatus
        )
      },
      text = stringResource(
        if (recipe.isSaved)
          R.string.common_recipe_control_screen_remove_from_recipe_book
        else
          R.string.common_recipe_control_screen_add_to_recipe_book
      ),
      iconId = if (recipe.isSaved) R.drawable.ic_added_to_recipes else R.drawable.ic_add_to_recipes,
      isFirst = true,
      isLast = true,
    )

    RecipeActionButton(
      onClick = { onIntent(RecipeControlScreenIntent.EditRecipe) },
      text = stringResource(R.string.common_recipe_control_screen_edit_recipe),
      iconId = R.drawable.ic_edit,
      isFirst = true,
    )
    RecipeActionButton(
      onClick = { onIntent(RecipeControlScreenIntent.OpenDeleteDialog) },
      text = stringResource(R.string.common_recipe_control_screen_delete_recipe),
      textTint = colors.tintPrimary,
      iconId = R.drawable.ic_trash,
      isLast = true,
    )
  }
}
