package io.chefbook.features.category.ui.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.result.ResultBackNavigator
import io.chefbook.features.category.R
import io.chefbook.features.category.ui.input.mvi.CategoryInputDialogEffect
import io.chefbook.features.category.ui.input.mvi.CategoryInputDialogIntent
import io.chefbook.navigation.navigators.DialogNavigator
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.navigation.results.category.CategoryActionResult
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import io.chefbook.navigation.styles.DismissibleDialog
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Destination(route = "category/input", style = DismissibleDialog::class)
@Composable
fun CategoryInputDialog(
  categoryId: String? = null,
  categoryInputDialogNavigator: DialogNavigator,
  categoryInputResultNavigator: ResultBackNavigator<CategoryActionResult>,
  confirmDialogResult: OpenResultRecipient<TwoButtonsDialogResult>,
) {
  val viewModel: ICategoryInputDialogViewModel =
    getViewModel<CategoryInputDialogViewModel> { parametersOf(categoryId) }
  val state = viewModel.state.collectAsState()

  CategoryInputDialogContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  confirmDialogResult.onNavResult { result ->
    if (result is NavResult.Value && result.value is TwoButtonsDialogResult.RightButtonClicked) {
      viewModel.handleIntent(CategoryInputDialogIntent.ConfirmDelete)
    }
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is CategoryInputDialogEffect.Cancel -> categoryInputResultNavigator.navigateBack()
        is CategoryInputDialogEffect.CategoryCreated -> {
          categoryInputResultNavigator.navigateBack(
            result = CategoryActionResult.Created(
              id = effect.category.id,
              name = effect.category.name,
              cover = effect.category.emoji,
            )
          )
        }

        is CategoryInputDialogEffect.CategoryUpdated -> {
          categoryInputResultNavigator.navigateBack(
            result = CategoryActionResult.Updated(
              id = effect.category.id,
              name = effect.category.name,
              cover = effect.category.emoji,
            )
          )
        }

        is CategoryInputDialogEffect.OpenDeleteConfirmation -> {
          categoryInputDialogNavigator.openTwoButtonsDialog(
            TwoButtonsDialogParams(
              descriptionId = R.string.common_category_screen_category_delete_warning
            )
          )
        }

        is CategoryInputDialogEffect.CategoryDeleted -> {
          categoryInputResultNavigator.navigateBack(result = CategoryActionResult.Deleted(categoryId = effect.categoryId))
        }
      }
    }
  }
}
