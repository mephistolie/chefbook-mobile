package com.cactusknights.chefbook.ui.screens.recipeinput

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.mappers.LanguageMapper
import com.cactusknights.chefbook.core.ui.DataAccessProvider
import com.cactusknights.chefbook.core.ui.DataType
import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.navigation.hosts.RecipeInputHost
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.RecipeSavedDialog
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEffect
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.dialogs.LoadingDialog
import com.cactusknights.chefbook.ui.views.dialogs.TwoButtonsDialog
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun RecipeInputScreen(
    recipeId: Int?,
    defaultLanguage: String,
    appController: NavHostController,
    viewModel: RecipeInputScreenViewModel = hiltViewModel(),
) {
    val detailsRoute = Destination.RecipeInput.Details.route
    val ingredientsRoute = Destination.RecipeInput.Ingredients.route
    val cookingRoute = Destination.RecipeInput.Cooking.route

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val bottomSheetNavigator = remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
    val inputController = rememberAnimatedNavController()
    inputController.navigatorProvider.addNavigator(bottomSheetNavigator)

    val state = viewModel.state.collectAsState()

    DataAccessProvider(type = DataType.DECRYPTABLE) {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp),
            sheetBackgroundColor = ChefBookTheme.colors.backgroundPrimary,
        ) {
            RecipeInputHost(
                viewModel = viewModel,
                appController = appController,
                inputController = inputController,
                sheetState
            )

            val currentState = state.value
            if (currentState.isCancelDialogOpen) {
                TwoButtonsDialog(
                    description = stringResource(R.string.common_recipe_input_screen_close_warning),
                    onHide = { viewModel.obtainEvent(RecipeInputScreenEvent.ChangeCancelDialogState(false)) },
                    onRightClick = { viewModel.obtainEvent(RecipeInputScreenEvent.Back) },
                )
            }
            if (currentState.isLoadingDialogOpen) LoadingDialog()
            if (currentState.isRecipeSavedDialogOpen && currentState.recipeId != null) {
                RecipeSavedDialog(
                    onOpenRecipe = { viewModel.obtainEvent(RecipeInputScreenEvent.OpenRecipe(currentState.recipeId)) },
                    onBackToRecipes = { viewModel.obtainEvent(RecipeInputScreenEvent.Close) }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        val language = LanguageMapper.map(defaultLanguage)
        if (language != Language.OTHER) {
            viewModel.obtainEvent(RecipeInputScreenEvent.SetLanguage(language))
        }
        if (recipeId != null) {
            viewModel.obtainEvent(RecipeInputScreenEvent.SetRecipe(recipeId))
        }
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is RecipeInputScreenEffect.OnVisibilityPickerOpen -> {
                    inputController.navigate(Destination.RecipeInput.Details.Visibility.route)
                }
                is RecipeInputScreenEffect.OnEncryptionStatePickerOpen -> {
                    inputController.navigate(Destination.RecipeInput.Details.Encryption.route)
                }
                is RecipeInputScreenEffect.OnEncryptedVaultMenuOpen -> {
                    appController.navigate(Destination.Encryption.route(closeOnUnlocked = true))
                }
                is RecipeInputScreenEffect.OnLanguagePickerOpen -> {
                    inputController.navigate(Destination.RecipeInput.Details.Language.route)
                }
                is RecipeInputScreenEffect.OnCaloriesDialogOpen -> {
                    inputController.navigate(Destination.RecipeInput.Details.Calories.route)
                }
                is RecipeInputScreenEffect.OnIngredientDialogOpen -> {
                    inputController.navigate(Destination.RecipeInput.Ingredients.Ingredient.route(effect.index))
                }
                is RecipeInputScreenEffect.OnBottomSheetClosed -> {
                    sheetState.hide()
                }
                is RecipeInputScreenEffect.OnContinue -> {
                    val pageRoute = when (inputController.currentDestination?.route) {
                        detailsRoute -> ingredientsRoute
                        ingredientsRoute -> cookingRoute
                        else -> null
                    }
                    if (pageRoute != null) inputController.navigate(pageRoute)
                }
                is RecipeInputScreenEffect.OnBack -> {
                    if (inputController.previousBackStackEntry != null) {
                        inputController.popBackStack()
                    } else {
                        appController.popBackStack()
                    }
                }
                is RecipeInputScreenEffect.OnOpenRecipe -> {
                    appController.apply {
                        popBackStack()
                        navigate(Destination.Recipe.route(effect.id))
                    }
                }
                is RecipeInputScreenEffect.OnClose -> {
                    appController.popBackStack()
                }
            }
        }
    }

}
