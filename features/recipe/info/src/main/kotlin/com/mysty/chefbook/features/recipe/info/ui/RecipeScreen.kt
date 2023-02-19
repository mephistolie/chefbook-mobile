package com.mysty.chefbook.features.recipe.info.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.core.android.compose.providers.ContentType
import com.mysty.chefbook.core.android.showToast
import com.mysty.chefbook.design.theme.EncryptedDataTheme
import com.mysty.chefbook.features.recipe.control.ui.navigation.IRecipeControlScreenNavigator
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenEffect
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import com.mysty.chefbook.features.recipe.info.ui.navigation.IRecipeScreenNavigator
import com.mysty.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import com.mysty.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import com.mysty.chefbook.ui.common.presentation.RecipeScreenPage
import com.mysty.chefbook.ui.common.providers.RecipeEncryptionProvider
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialApi::class)
@Destination(
  route = "info",
  deepLinks = [
    DeepLink(uriPattern = "http://chefbook.space/recipes/{recipeId}"),
    DeepLink(uriPattern = "https://chefbook.space/recipes/{recipeId}")
  ],
  style = DestinationStyle.BottomSheet::class
)
@Composable
fun RecipeScreen(
  recipeId: String,
  initPage: RecipeScreenPage = RecipeScreenPage.INGREDIENTS,
  openExpanded: Boolean = false,
  navigator: IRecipeScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>
) {
  val viewModel: IRecipeScreenViewModel =
    getViewModel<RecipeScreenViewModel> { parametersOf(recipeId) }
  val state = viewModel.state.collectAsState()

  val context = LocalContext.current
  val scope = rememberCoroutineScope()


  val sheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true
  )

  RecipeEncryptionProvider(
    encryption = (state.value as? RecipeScreenState.Success)?.recipe?.encryptionState
      ?: EncryptionState.Standard
  ) {
    EncryptedDataTheme(
      isEncrypted = (state.value as? RecipeScreenState.Success)?.recipe?.encryptionState is EncryptionState.Decrypted
    ) {
      RecipeScreenBottomSheet(
        state = state.value,
        initPage = initPage,
        onIntent = { event -> viewModel.handleIntent(event) },
        sheetState = sheetState,
        controlNavigator = getRecipeControlScreenNavigator(
          navigator = navigator,
          onNavigateUp = { scope.launch { sheetState.hide() } }
        ),
        confirmDialogRecipient = confirmDialogRecipient,
        openExpanded = openExpanded,
      )
    }
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeScreenEffect.ShowToast -> context.showToast(effect.messageId)
        is RecipeScreenEffect.OpenBottomSheet -> sheetState.show()
        is RecipeScreenEffect.Close -> navigator.closeRecipeScreen()
        is RecipeScreenEffect.OpenShareDialog -> navigator.openRecipeShareDialog(recipeId = effect.recipeId)
        is RecipeScreenEffect.OpenCategoryScreen -> navigator.openCategoryRecipesScreen(effect.categoryId)
        is RecipeScreenEffect.OpenPicturesViewer -> {
          val encryptionState = (state.value as? RecipeScreenState.Success)?.recipe?.encryptionState
            ?: EncryptionState.Standard
          navigator.openPicturesViewer(
            pictures = effect.pictures.toTypedArray(),
            startIndex = effect.startIndex,
            picturesType = if (encryptionState is EncryptionState.Decrypted) ContentType.DECRYPTABLE else ContentType.DECRYPTED
          )
        }
      }
    }
  }
}

fun getRecipeControlScreenNavigator(
  navigator: IRecipeScreenNavigator,
  onNavigateUp: () -> Unit,
) = object : IRecipeControlScreenNavigator {

  override fun navigateUp(skipAnimation: Boolean) = onNavigateUp()

  override fun openTwoButtonsDialog(params: TwoButtonsDialogParams, request: String) {
    navigator.openTwoButtonsDialog(params, request)
  }

  override fun openRecipeInputScreen(recipeId: String?) =
    navigator.openRecipeInputScreen(recipeId = recipeId)

}