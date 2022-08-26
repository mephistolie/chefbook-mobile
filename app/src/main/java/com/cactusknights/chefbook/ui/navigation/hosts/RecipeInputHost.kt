package com.cactusknights.chefbook.ui.navigation.hosts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.navigation.INGREDIENT_INDEX
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.CaloriesDialog
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.EncryptionStatePickerDialog
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.IngredientDialog
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.LanguagePickerDialog
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.VisibilityPickerDialog
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking.RecipeInputCookingScreen
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.RecipeInputDetailsScreen
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.RecipeInputIngredientScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun RecipeInputHost(
    viewModel: RecipeInputScreenViewModel,
    appController: NavHostController,
    inputController: NavHostController,
    sheetState: ModalBottomSheetState,
) =
    AnimatedNavHost(
        navController = inputController,
        startDestination = Destination.RecipeInput.Details.route,
        enterTransition = {
            slideInHorizontally(animationSpec = tween(300)) { it }
        },
        exitTransition = {
            slideOutHorizontally(animationSpec = tween(300)) { -it }
        },
        popEnterTransition = {
            slideInHorizontally(animationSpec = tween(300)) { -it }
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = tween(300)) { it }
        },
    ) {
        composable(
            route = Destination.RecipeInput.Details.route
        ) {
            RecipeInputDetailsScreen(viewModel)
        }
        composable(
            route = Destination.RecipeInput.Ingredients.route
        ) {
            RecipeInputIngredientScreen(viewModel)
        }
        composable(
            route = Destination.RecipeInput.Cooking.route
        ) {
            RecipeInputCookingScreen(viewModel)
        }
        bottomSheet(
            route = Destination.RecipeInput.Details.Visibility.route
        ) {
            VisibilityPickerDialog(viewModel)
        }
        bottomSheet(
            route = Destination.RecipeInput.Details.Language.route
        ) {
            LanguagePickerDialog(viewModel)
        }
        bottomSheet(
            route = Destination.RecipeInput.Details.Encryption.route
        ) {
            EncryptionStatePickerDialog(viewModel)
        }
        bottomSheet(
            route = Destination.RecipeInput.Details.Calories.route
        ) {
            CaloriesDialog(viewModel)
        }
        bottomSheet(
            route = Destination.RecipeInput.Ingredients.Ingredient.route,
            arguments = listOf(navArgument(INGREDIENT_INDEX) { type = NavType.IntType }),
        ) { backStack ->
            backStack.arguments?.getInt(INGREDIENT_INDEX)?.let { index ->
                IngredientDialog(
                    ingredientIndex = index,
                    viewModel = viewModel,
                )
            }

        }
    }

