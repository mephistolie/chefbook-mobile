package io.chefbook.navigation.hosts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.manualcomposablecalls.bottomSheetComposable
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.manualcomposablecalls.dialogComposable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.resultBackNavigator
import com.ramcosta.composedestinations.scope.resultRecipient
import io.chefbook.features.auth.ui.AuthScreen
import io.chefbook.features.auth.ui.destinations.AuthScreenDestination
import io.chefbook.features.category.ui.input.CategoryInputScreen
import io.chefbook.features.category.ui.input.destinations.CategoryInputScreenDestination
import io.chefbook.features.profile.control.ui.ProfileScreen
import io.chefbook.features.profile.control.ui.destinations.ProfileScreenDestination
import io.chefbook.features.recipe.control.ui.RecipeControlScreen
import io.chefbook.features.recipe.control.ui.destinations.RecipeControlScreenDestination
import io.chefbook.features.recipe.info.ui.RecipeScreen
import io.chefbook.features.recipe.info.ui.destinations.RecipeScreenDestination
import io.chefbook.features.recipe.input.ui.destinations.RecipeInputDetailsScreenDestination
import io.chefbook.features.recipe.input.ui.screens.details.RecipeInputDetailsScreen
import io.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import io.chefbook.features.recipebook.category.ui.CategoryRecipesScreen
import io.chefbook.features.recipebook.category.ui.destinations.CategoryRecipesScreenDestination
import io.chefbook.features.recipebook.dashboard.ui.destinations.DashboardScreenDestination as RecipeBookDashboardScreenDestination
import io.chefbook.navigation.graphs.NavGraphs
import io.chefbook.navigation.hosts.dependencies.CommunityRecipesScreen
import io.chefbook.navigation.hosts.dependencies.RecipeInputScreenDependencies
import io.chefbook.navigation.hosts.dependencies.RecipeScreenDependencies
import io.chefbook.navigation.navigators.AppNavigator
import io.chefbook.navigation.results.category.CategoryActionResult
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import io.chefbook.ui.common.dialogs.destinations.DismissibleTwoButtonsDialogDestination
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun AppHost(
  isSignedIn: Boolean,
  navigator: AppNavigator,
) {
  DestinationsNavHost(
    navGraph = NavGraphs.root,
    startRoute = if (isSignedIn) RecipeBookDashboardScreenDestination else AuthScreenDestination,
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
      RecipeScreenDependencies()
      RecipeInputScreenDependencies()
      CommunityRecipesScreen()
    }
  ) {
    composable(AuthScreenDestination) {
      AuthScreen(
        userId = navArgs.userId,
        activationCode = navArgs.activationCode,
        passwordResetCode = navArgs.passwordResetCode,
        navigator = navigator,
        confirmDialogRecipient = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
    composable(ProfileScreenDestination) {
      ProfileScreen(
        navigator = navigator,
        confirmDialogResult = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
    composable(CategoryRecipesScreenDestination) {
      CategoryRecipesScreen(
        categoryId = navArgs.categoryId,
        isTag = navArgs.isTag,
        navigator = navigator,
        categoryInputRecipient = resultRecipient<CategoryInputScreenDestination, CategoryActionResult>()
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
        viewModel = koinViewModel<RecipeInputScreenViewModel>(viewModelStoreOwner = parentEntry) {
          parametersOf(recipeId)
        },
        navigator = navigator,
        confirmDialogRecipient = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
    dialogComposable(CategoryInputScreenDestination) {
      CategoryInputScreen(
        categoryId = navArgs.categoryId,
        categoryInputDialogNavigator = navigator,
        categoryInputResultNavigator = resultBackNavigator(),
        confirmDialogResult = resultRecipient<DismissibleTwoButtonsDialogDestination, TwoButtonsDialogResult>()
      )
    }
  }
}
