package io.chefbook.features.recipebook.category.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenEffect
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenIntent
import io.chefbook.features.recipebook.category.ui.navigation.CategoryRecipesScreenNavigator
import io.chefbook.navigation.results.category.CategoryActionResult
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination(route = "recipe_book/category")
@Composable
fun CategoryRecipesScreen(
  categoryId: String,
  isTag: Boolean,
  navigator: CategoryRecipesScreenNavigator,
  categoryInputRecipient: OpenResultRecipient<CategoryActionResult>
) {
  val viewModel = koinViewModel<CategoryRecipesScreenViewModel> { parametersOf(categoryId, isTag) }
  val state = viewModel.state.collectAsStateWithLifecycle()

  CategoryScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  categoryInputRecipient.onNavResult { navResult ->
    if (navResult is NavResult.Value) {
      when (val result = navResult.value) {
        is CategoryActionResult.Updated -> {
          viewModel.handleIntent(
            CategoryScreenIntent.OnCategoryUpdated(
              io.chefbook.sdk.category.api.external.domain.entities.Category(
                id = result.id,
                name = result.name,
                emoji = result.cover,
              )
            )
          )
        }

        is CategoryActionResult.Deleted -> viewModel.handleIntent(CategoryScreenIntent.Back)
        else -> Unit
      }
    }
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is CategoryScreenEffect.OpenRecipeScreen -> navigator.openRecipeScreen(recipeId = effect.recipeId)
        is CategoryScreenEffect.OpenCategoryInputDialog -> navigator.openCategoryInputScreen(
          categoryId = effect.categoryId
        )

        is CategoryScreenEffect.Back -> navigator.navigateUp()
      }
    }
  }

}
