package io.chefbook.features.recipebook.creation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.design.components.bottomsheet.BottomSheetBox
import io.chefbook.design.components.bottomsheet.BottomSheetColumn
import io.chefbook.design.components.bottomsheet.PullBarType
import io.chefbook.core.android.R as coreR
import io.chefbook.features.recipebook.creation.ui.mvi.RecipeBookCreationScreenIntent
import io.chefbook.ui.common.components.menu.MenuItem

@Composable
internal fun RecipeControlScreenContent(
  onIntent: (RecipeBookCreationScreenIntent) -> Unit,
) {

  BottomSheetBox(
    pullBarType = PullBarType.EXTERNAl,
  ) {
    Column(
      modifier = Modifier
        .navigationBarsPadding()
        .padding(horizontal = 16.dp),
    ) {
      MenuItem(
        title = stringResource(coreR.string.common_general_recipe),
        onClick = { onIntent(RecipeBookCreationScreenIntent.RecipeInputButtonClicked) },
      )
      MenuItem(
        title = stringResource(coreR.string.common_general_category),
        onClick = { onIntent(RecipeBookCreationScreenIntent.CategoryInputButtonClicked) },
      )
    }
  }
}
