package com.mysty.chefbook.features.recipe.info.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.shapes.ModalBottomSheetShape
import com.mysty.chefbook.features.recipe.control.ui.RecipeControlScreen
import com.mysty.chefbook.features.recipe.control.ui.navigation.IRecipeControlScreenNavigator
import com.mysty.chefbook.features.recipe.info.ui.components.common.RecipeScreenErrorContent
import com.mysty.chefbook.features.recipe.info.ui.components.common.content.RecipeScreenLoadingContent
import com.mysty.chefbook.features.recipe.info.ui.components.common.content.loaded.RecipeScreenLoadedContent
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import com.mysty.chefbook.features.recipe.info.ui.presentation.RecipeScreenBottomSheetType
import com.mysty.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import com.mysty.chefbook.ui.common.presentation.RecipeScreenPage
import com.ramcosta.composedestinations.result.OpenResultRecipient

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun RecipeScreenBottomSheet(
  state: RecipeScreenState,
  initPage: RecipeScreenPage,
  onIntent: (RecipeScreenIntent) -> Unit,
  controlNavigator: IRecipeControlScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>,
  sheetState: ModalBottomSheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true
  ),
  openExpanded: Boolean = false,
) {
  val screenHeightWithPaddings = LocalConfiguration.current.screenHeightDp.dp
  val navigationBarsPadding =
    WindowInsets.Companion.navigationBars.asPaddingValues().calculateBottomPadding()
  val bottomSheetHeight = screenHeightWithPaddings + navigationBarsPadding

  val colors = LocalTheme.colors

  ModalBottomSheetLayout(
    modifier = Modifier
      .height(bottomSheetHeight)
      .background(colors.backgroundSecondary),
    sheetState = sheetState,
    sheetShape = ModalBottomSheetShape,
    sheetBackgroundColor = colors.backgroundPrimary,
    sheetElevation = 0.dp,
    sheetContent = {
      Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
        (state as? RecipeScreenState.Success)?.bottomSheetType?.let { type ->
          if (sheetState.isVisible) {
            when (type) {
              RecipeScreenBottomSheetType.MENU -> {
                RecipeControlScreen(
                  recipeId = state.recipe.id,
                  navigator = controlNavigator,
                  confirmDialogRecipient = confirmDialogRecipient,
                )
              }
              RecipeScreenBottomSheetType.DETAILS -> {}
            }
          }
        }
      }
    }
  ) {
    Box {
      when (state) {
        is RecipeScreenState.Loading -> RecipeScreenLoadingContent()
        is RecipeScreenState.Success -> RecipeScreenLoadedContent(
          state = state,
          initPage = initPage,
          onIntent = onIntent,
          openExpanded = openExpanded,
          screenHeight = bottomSheetHeight,
        )
        is RecipeScreenState.Error -> RecipeScreenErrorContent(
          onReloadClick = { onIntent(RecipeScreenIntent.ReloadRecipe) },
          onCloseClick = { onIntent(RecipeScreenIntent.Close) }
        )
      }
    }
  }
}
