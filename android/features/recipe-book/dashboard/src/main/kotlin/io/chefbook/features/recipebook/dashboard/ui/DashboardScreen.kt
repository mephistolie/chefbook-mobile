package io.chefbook.features.recipebook.dashboard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenEffect
import io.chefbook.features.recipebook.dashboard.ui.navigation.DashboardScreenNavigator
import org.koin.androidx.compose.getViewModel

@Destination("recipe_book/dashboard")
@Composable
fun DashboardScreen(
  navigator: DashboardScreenNavigator,
) {
  val viewModel: IRecipeBookScreenViewModel = getViewModel<DashboardScreenViewModel>()
  val state = viewModel.state.collectAsState()

  DashboardScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is DashboardScreenEffect.ProfileScreenOpened -> navigator.openProfileScreen()
        is DashboardScreenEffect.RecipeInputScreenOpened -> navigator.openRecipeInputScreen()

        is DashboardScreenEffect.RecipeSearchScreenOpened -> navigator.openRecipeBookSearchScreen()
        is DashboardScreenEffect.FavouriteRecipesScreenOpened -> navigator.openFavouriteRecipesScreen()
        is DashboardScreenEffect.CategoryRecipesScreenOpened -> navigator.openCategoryRecipesScreen(effect.categoryId)

        is DashboardScreenEffect.CommunityRecipesScreenOpened -> navigator.openCommunityRecipesScreen()
        is DashboardScreenEffect.OpenEncryptedVaultScreen -> navigator.openEncryptedVaultScreen()
        is DashboardScreenEffect.ShoppingListScreenOpened -> navigator.openShoppingListScreen()
        is DashboardScreenEffect.RecipeScreenOpened -> navigator.openRecipeScreen(recipeId = effect.recipeId)
        is DashboardScreenEffect.CategoryCreationScreenOpened -> navigator.openCategoryInputDialog()
      }
    }
  }
}
