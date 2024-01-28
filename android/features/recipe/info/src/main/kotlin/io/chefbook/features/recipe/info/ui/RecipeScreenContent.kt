package io.chefbook.features.recipe.info.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.features.recipe.control.navigation.RecipeControlScreenNavigator
import io.chefbook.features.recipe.control.ui.RecipeControlScreen
import io.chefbook.features.recipe.info.ui.components.common.content.RecipeScreenErrorContent
import io.chefbook.features.recipe.info.ui.components.common.content.RecipeScreenLoadingContent
import io.chefbook.features.recipe.info.ui.components.common.content.loaded.RecipeScreenLoadedContent
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import io.chefbook.features.recipe.info.ui.state.RecipeScreenBottomSheetType
import io.chefbook.ui.common.presentation.RecipeScreenPage
import com.ramcosta.composedestinations.result.OpenResultRecipient
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.ModalBottomSheetShape
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun RecipeScreenBottomSheet(
  state: RecipeScreenState,
  initPage: RecipeScreenPage,
  onIntent: (RecipeScreenIntent) -> Unit,
  controlNavigator: RecipeControlScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>,
  sheetState: BottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed),
  modalSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
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
      .clippedBackground(colors.backgroundSecondary, shape = ModalBottomSheetShape),
    sheetState = modalSheetState,
    sheetBackgroundColor = Color.Transparent,
    sheetElevation = 0.dp,
    sheetContent = {
      Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.BottomCenter,
      ) {
        if (modalSheetState.isVisible) {
          (state as? RecipeScreenState.Success)?.bottomSheetType?.let { type ->
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
          sheetState = sheetState,
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
