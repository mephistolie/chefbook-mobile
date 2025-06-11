package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.design.icons.ArrowDownMedium
import io.chefbook.design.icons.ChefBookIcons
import io.chefbook.features.recipe.info.R
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.core.res as CoreR
import io.chefbook.design.R as designR

@Composable
@NonRestartableComposable
internal fun ManagementButton(
  recipe: Recipe,
  isPreviewLoaded: MutableState<Boolean>,
  onSaveClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionsWidgetButton(
    preview = recipe.preview,
    isPreviewLoaded = isPreviewLoaded,
    onClick = onSaveClick,
    modifier = modifier,
    text = stringResource(
      when {
        recipe.isFavourite -> R.string.common_recipe_screen_in_favourite
        recipe.isSaved -> CoreR.string.common_general_saved
        recipe.isOwned -> R.string.common_recipe_screen_management
        else -> CoreR.string.common_general_save
      }
    ),
    leftIconId = when {
      recipe.isFavourite -> designR.drawable.ic_favourite
      recipe.isSaved -> designR.drawable.ic_bookmark_fill
      else -> null
    },
    rightIcon = when {
      recipe.isSaved || recipe.isOwned -> ChefBookIcons.ArrowDownMedium
      else -> null
    },
    rightIconModifier = Modifier.padding(top = 2.dp),
    isSelected = recipe.isSaved,
  )
}
