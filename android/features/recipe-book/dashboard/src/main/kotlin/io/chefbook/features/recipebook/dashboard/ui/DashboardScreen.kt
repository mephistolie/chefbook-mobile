package io.chefbook.features.recipebook.dashboard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenEffect
import io.chefbook.features.recipebook.dashboard.ui.navigation.DashboardScreenNavigator
import org.koin.androidx.compose.koinViewModel

@Destination("recipe_book/dashboard")
@Composable
fun DashboardScreen(
  navigator: DashboardScreenNavigator,
) {
  val viewModel: IRecipeBookScreenViewModel = koinViewModel<DashboardScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  DashboardScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is DashboardScreenEffect.ProfileScreenOpened -> navigator.openProfileScreen()
        is DashboardScreenEffect.CreationScreenOpened -> navigator.openRecipeBookCreationScreen()

        is DashboardScreenEffect.RecipeSearchScreenOpened -> navigator.openRecipeBookSearchScreen()
        is DashboardScreenEffect.FavouriteRecipesScreenOpened -> navigator.openFavouriteRecipesScreen()
        is DashboardScreenEffect.CategoryRecipesScreenOpened -> navigator.openCategoryRecipesScreen(effect.categoryId)

        is DashboardScreenEffect.CommunityRecipesScreenOpened -> navigator.openCommunityRecipesScreen()
        is DashboardScreenEffect.OpenEncryptedVaultScreen -> navigator.openEncryptedVaultScreen()
        is DashboardScreenEffect.ShoppingListScreenOpened -> navigator.openShoppingListScreen()
        is DashboardScreenEffect.RecipeScreenOpened -> navigator.openRecipeScreen(recipeId = effect.recipeId)
        is DashboardScreenEffect.CategoriesScreenOpened -> navigator.openCategoriesScreen()
      }
    }
  }
}
