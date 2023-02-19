package com.cactusknights.chefbook.navigation.hosts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cactusknights.chefbook.navigation.graphs.NavGraphs
import com.cactusknights.chefbook.navigation.hosts.dependencies.HomeScreenDependencies
import com.cactusknights.chefbook.navigation.hosts.dependencies.RecipeInputScreenDependencies
import com.cactusknights.chefbook.navigation.hosts.dependencies.RecipeScreenDependencies
import com.cactusknights.chefbook.navigation.navigators.AppNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mysty.chefbook.features.category.ui.input.CategoryInputDialog
import com.mysty.chefbook.features.category.ui.input.destinations.CategoryInputDialogDestination
import com.mysty.chefbook.features.recipe.control.ui.RecipeControlScreen
import com.mysty.chefbook.features.recipe.control.ui.destinations.RecipeControlScreenDestination
import com.mysty.chefbook.features.recipe.info.ui.RecipeScreen
import com.mysty.chefbook.features.recipe.info.ui.destinations.RecipeScreenDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.RecipeInputDetailsScreenDestination
import com.mysty.chefbook.features.recipe.input.ui.screens.details.RecipeInputDetailsScreen
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import com.mysty.chefbook.features.recipebook.category.ui.CategoryRecipesScreen
import com.mysty.chefbook.features.recipebook.category.ui.destinations.CategoryRecipesScreenDestination
import com.mysty.chefbook.navigation.results.category.CategoryActionResult
import com.mysty.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import com.mysty.chefbook.ui.common.dialogs.destinations.DismissibleTwoButtonsDialogDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.manualcomposablecalls.bottomSheetComposable
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.resultBackNavigator
import com.ramcosta.composedestinations.scope.resultRecipient
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun AppHost(
  navigator: AppNavigator,
) {
  DestinationsNavHost(
    navGraph = NavGraphs.root,
    engine = rememberAnimatedNavHostEngine(
      rootDefaultAnimations = RootNavGraphDefaultAnimations(
        enterTransition = { slideInHorizontally(animationSpec = tween(300)) { it } },
        exitTransition = { slideOutHorizontally(animationSpec = tween(300)) { -it } },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(300)) { -it } },
        popExitTransition = { slideOutHorizontally(animationSpec = tween(300)) { it } },
      ),
    ),
    navController = navigator.navController,
    dependenciesContainerBuilder = {
      dependency(navigator)
      HomeScreenDependencies(navigator = navigator)
      RecipeScreenDependencies()
      RecipeInputScreenDependencies()
    }
  ) {
    composable(CategoryRecipesScreenDestination) {
      CategoryRecipesScreen(
        categoryId = navArgs.categoryId,
        navigator = navigator,
        categoryInputRecipient = resultRecipient<CategoryInputDialogDestination, CategoryActionResult>()
      )
    }
    bottomSheetComposable(RecipeScreenDestination) {
      RecipeScreen(
        recipeId = navArgs.recipeId,
        initPage = navArgs.initPage,
        openExpanded = navArgs.openExpanded,
        navigator = navigator,
        confirmDialogRecipient = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
    bottomSheetComposable(RecipeControlScreenDestination) {
      RecipeControlScreen(
        recipeId = navArgs.recipeId,
        navigator = navigator,
        confirmDialogRecipient = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
    composable(RecipeInputDetailsScreenDestination) {
      val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(NavGraphs.recipeInput.route)
      }
      val recipeId = parentEntry.arguments?.getString(NavGraphs.RECIPE_ID_ARGUMENT)
      RecipeInputDetailsScreen(
        viewModel = getViewModel<RecipeInputScreenViewModel>(viewModelStoreOwner = parentEntry) {
          parametersOf(recipeId)
        },
        navigator = navigator,
        confirmDialogRecipient = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
    composable(CategoryInputDialogDestination) {
      CategoryInputDialog(
        categoryId = navArgs.categoryId,
        categoryInputDialogNavigator = navigator,
        categoryInputResultNavigator = resultBackNavigator(),
        confirmDialogResult = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
  }
}
