package com.mysty.chefbook.features.recipe.info.ui.components.common.actions

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.features.recipe.info.R

@Composable
internal fun ManagementButton(
  recipe: Recipe,
  onSaveClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionsWidgetButton(
    onClick = onSaveClick,
    modifier = modifier,
    text = stringResource(
      when {
        recipe.isFavourite -> R.string.common_recipe_screen_in_favourite
        recipe.isSaved -> R.string.common_general_saved
        recipe.isOwned -> R.string.common_recipe_screen_management
        else -> R.string.common_general_save
      }
    ),
    leftIconId = when {
      recipe.isFavourite -> R.drawable.ic_favourite
      recipe.isSaved -> R.drawable.ic_added_to_recipes
      else -> null
    },
    rightIconId = when {
      recipe.isSaved || recipe.isOwned -> R.drawable.ic_arrow_down
      else -> null
    },
    rightIconModifier = Modifier.padding(top = 2.dp),
    isSelected = recipe.isSaved,
  )
}
