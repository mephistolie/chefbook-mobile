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
import com.cactusknights.chefbook.domain.entities.settings.Settings
import com.cactusknights.chefbook.ui.navigation.CATEGORY_ID_ARGUMENT
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.navigation.RECIPE_ID_ARGUMENT
import com.cactusknights.chefbook.ui.screens.auth.AuthScreen
import com.cactusknights.chefbook.ui.screens.category.CategoryScreen
import com.cactusknights.chefbook.ui.screens.favourite.FavouriteScreen
import com.cactusknights.chefbook.ui.screens.home.HomeScreen
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import com.cactusknights.chefbook.ui.screens.recipe.RecipeScreen
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreen
import com.cactusknights.chefbook.ui.screens.search.RecipeBookSearchScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun RootHost(
    settings: Settings,
    appState: AppState,
    navController: NavHostController,
    sheetState: ModalBottomSheetState,
) =
        AnimatedNavHost(
            navController = navController,
            startDestination = Destination.Home.route,
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
                route = Destination.Auth.route
            ) {
                AuthScreen(navController)
            }
            composable(
                route = Destination.Home.route
            ) {
                HomeScreen(
                    appState = appState,
                    appController = navController,
                )
            }
            composable(
                route = Destination.Home.RecipeBook.Search.route,
            ) {
                RecipeBookSearchScreen(navController)
            }
            composable(
                route = Destination.Home.RecipeBook.Favourite.route,
            ) {
                FavouriteScreen(navController)
            }
            composable(
                route = Destination.Home.RecipeBook.Category.route,
                arguments = listOf(navArgument(CATEGORY_ID_ARGUMENT) { type = NavType.IntType }),
            ) { backStack ->
                backStack.arguments?.getInt(CATEGORY_ID_ARGUMENT)?.let { categoryId ->
                    CategoryScreen(
                        categoryId = categoryId,
                        appController = navController,
                    )
                }
            }
            bottomSheet(
                route = Destination.Recipe.route,
                arguments = listOf(
                    navArgument(RECIPE_ID_ARGUMENT) { type = NavType.IntType }
                ),
                deepLinks = Destination.Recipe.deeplinks
            ) { backStack ->
                backStack.arguments?.getInt(RECIPE_ID_ARGUMENT)?.let { recipeId ->
                    RecipeScreen(
                        recipeId = recipeId,
                        navController = navController,
                        sheetState = sheetState,
                    )
                }
            }
            composable(
                route = Destination.RecipeInput.route,
                arguments = listOf(
                    navArgument(RECIPE_ID_ARGUMENT) {
                        nullable = true
                        type = NavType.StringType
                    }
                ),
            ) { backStack ->
                val recipeId = backStack.arguments?.getString(RECIPE_ID_ARGUMENT)?.toIntOrNull()
                RecipeInputScreen(
                    recipeId = recipeId,
                    defaultLanguage = settings.defaultRecipeLanguage,
                    appController = navController,
                )
            }
        }