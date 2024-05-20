package io.chefbook.features.category.ui.input

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.result.ResultBackNavigator
import io.chefbook.features.category.R
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenEffect
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenIntent
import io.chefbook.navigation.navigators.DialogNavigator
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.navigation.results.category.CategoryActionResult
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import io.chefbook.navigation.styles.DismissibleDialog
import io.chefbook.navigation.styles.NonDismissibleDialog
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination(route = "category/input", style = NonDismissibleDialog::class)
@Composable
fun CategoryInputScreen(
  categoryId: String? = null,
  categoryInputDialogNavigator: DialogNavigator,
  categoryInputResultNavigator: ResultBackNavigator<CategoryActionResult>,
  confirmDialogResult: OpenResultRecipient<TwoButtonsDialogResult>,
) {
  val viewModel = koinViewModel<CategoryInputScreenViewModel> { parametersOf(categoryId) }
  val state = viewModel.state.collectAsStateWithLifecycle()

  CategoryInputScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  confirmDialogResult.onNavResult { result ->
    if (result is NavResult.Value && result.value is TwoButtonsDialogResult.RightButtonClicked) {
      viewModel.handleIntent(CategoryInputScreenIntent.ConfirmDelete)
    }
  }

  BackHandler {
    viewModel.handleIntent(CategoryInputScreenIntent.Cancel)
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is CategoryInputScreenEffect.Cancel -> categoryInputResultNavigator.navigateBack()
        is CategoryInputScreenEffect.CategoryCreated -> {
          categoryInputResultNavigator.navigateBack(
            result = CategoryActionResult.Created(
              id = effect.category.id,
              name = effect.category.name,
              cover = effect.category.emoji,
            )
          )
        }

        is CategoryInputScreenEffect.CategoryUpdated -> {
          categoryInputResultNavigator.navigateBack(
            result = CategoryActionResult.Updated(
              id = effect.category.id,
              name = effect.category.name,
              cover = effect.category.emoji,
            )
          )
        }

        is CategoryInputScreenEffect.OpenDeleteConfirmation -> {
          categoryInputDialogNavigator.openTwoButtonsDialog(
            TwoButtonsDialogParams(
              descriptionId = R.string.common_category_screen_category_delete_warning
            )
          )
        }

        is CategoryInputScreenEffect.CategoryDeleted -> {
          categoryInputResultNavigator.navigateBack(result = CategoryActionResult.Deleted(categoryId = effect.categoryId))
        }
      }
    }
  }
}
