package io.chefbook.features.recipe.control.ui.components.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.toolbar.Toolbar
import io.chefbook.design.icons.ArrowEnd
import io.chefbook.design.icons.ChefBookIcons
import io.chefbook.design.theme.dimens.IconSize16
import io.chefbook.features.recipe.control.R
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenIntent
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.design.R as designR

@Composable
internal fun RecipeControlScreenMenu(
  recipe: DecryptedRecipeInfo,
  onIntent: (RecipeControlScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Toolbar(
      leftButtonIcon = null,
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
        icon = ImageVector.vectorResource(
          if (recipe.isFavourite) designR.drawable.ic_favourite else designR.drawable.ic_unfavourite
        ),
        isFirst = true,
      )
      RecipeActionButton(
        onClick = { onIntent(RecipeControlScreenIntent.ChangeCategories) },
        text = stringResource(R.string.common_recipe_control_screen_choose_categories),
        icon = ChefBookIcons.ArrowEnd,
        iconSize = IconSize16,
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
      icon = ImageVector.vectorResource(
        if (recipe.isSaved) designR.drawable.ic_bookmark_fill else designR.drawable.ic_bookmark
      ),
      isFirst = true,
      isLast = true,
    )

    if (recipe.isOwned) {
      RecipeActionButton(
        onClick = { onIntent(RecipeControlScreenIntent.EditRecipe) },
        text = stringResource(R.string.common_recipe_control_screen_edit_recipe),
        icon = ImageVector.vectorResource(designR.drawable.ic_edit),
        isFirst = true,
      )
      RecipeActionButton(
        onClick = { onIntent(RecipeControlScreenIntent.OpenDeleteDialog) },
        text = stringResource(R.string.common_recipe_control_screen_delete_recipe),
        textTint = colors.tintPrimary,
        icon = ImageVector.vectorResource(designR.drawable.ic_trash),
        isLast = true,
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
  }
}
