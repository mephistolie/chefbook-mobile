package io.chefbook.features.recipe.info.ui

import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.core.android.compose.providers.ContentType
import io.chefbook.core.android.showToast
import io.chefbook.features.recipe.control.navigation.RecipeControlScreenNavigator
import io.chefbook.features.recipe.info.navigation.RecipeScreenNavigator
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenEffect
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.ui.common.presentation.RecipeScreenPage
import io.chefbook.ui.common.providers.RecipeEncryptionProvider
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialApi::class)
@Destination(
  route = "info",
  deepLinks = [DeepLink(uriPattern = "https://chefbook.io/recipes/{recipeId}")],
  style = DestinationStyleBottomSheet::class
)
@Composable
fun RecipeScreen(
  recipeId: String,
  initPage: RecipeScreenPage = RecipeScreenPage.INGREDIENTS,
  openExpanded: Boolean = false,
  navigator: RecipeScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>
) {
  val viewModel: IRecipeScreenViewModel =
    getViewModel<RecipeScreenViewModel> { parametersOf(recipeId) }
  val state = viewModel.state.collectAsState()

  val recipe = remember(state) { (state.value as? RecipeScreenState.Success)?.recipe }
  val isEncryptionEnabled = remember(recipe) { derivedStateOf { recipe?.isEncryptionEnabled ?: false } }
  val isDecrypted = remember(recipe) { derivedStateOf { recipe is Recipe.Decrypted } }

  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

  val modalSheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    animationSpec = TweenSpec(),
    skipHalfExpanded = true,
  )

  RecipeEncryptionProvider(
    isEncryptionEnabled = isEncryptionEnabled.value,
    isDecrypted = isDecrypted.value,
  ) {
    RecipeScreenBottomSheet(
      state = state.value,
      initPage = initPage,
      onIntent = { event -> viewModel.handleIntent(event) },
      sheetState = sheetState,
      modalSheetState = modalSheetState,
      controlNavigator = getRecipeControlScreenNavigator(
        navigator = navigator,
        onNavigateUp = { scope.launch { modalSheetState.hide() } }
      ),
      confirmDialogRecipient = confirmDialogRecipient,
      openExpanded = openExpanded,
    )
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeScreenEffect.ShowToast -> context.showToast(effect.messageId)
        is RecipeScreenEffect.ExpandSheet -> launch { sheetState.expand() }
        is RecipeScreenEffect.OpenModalBottomSheet -> modalSheetState.show()
        is RecipeScreenEffect.Close -> navigator.closeRecipeScreen()
        is RecipeScreenEffect.OpenShareDialog -> navigator.openRecipeShareDialog(recipeId = effect.recipeId)
        is RecipeScreenEffect.OpenCategoryScreen -> navigator.openCategoryRecipesScreen(effect.categoryId)
        is RecipeScreenEffect.OpenPicturesViewer -> {
          navigator.openPicturesViewer(
            pictures = effect.pictures.toTypedArray(),
            startIndex = effect.startIndex,
            picturesType = if (isEncryptionEnabled.value) ContentType.DECRYPTABLE else ContentType.DECRYPTED
          )
        }
      }
    }
  }
}

private fun getRecipeControlScreenNavigator(
  navigator: RecipeScreenNavigator,
  onNavigateUp: () -> Unit,
) = object : RecipeControlScreenNavigator {

  override fun navigateUp(skipAnimation: Boolean) = onNavigateUp()

  override fun openTwoButtonsDialog(params: TwoButtonsDialogParams, request: String) {
    navigator.openTwoButtonsDialog(params, request)
  }

  override fun openRecipeInputScreen(recipeId: String?) =
    navigator.openRecipeInputScreen(recipeId = recipeId)
}