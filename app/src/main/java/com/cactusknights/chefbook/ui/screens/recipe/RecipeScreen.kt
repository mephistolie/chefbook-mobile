package com.cactusknights.chefbook.ui.screens.recipe

import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cactusknights.chefbook.core.ui.RecipeEncryptionProvider
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState
import com.cactusknights.chefbook.ui.navigation.CATEGORY_ID_ARGUMENT
import com.cactusknights.chefbook.ui.navigation.Destination
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenEffect
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenState
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenTab
import com.cactusknights.chefbook.ui.screens.recipe.views.RecipeScreenDisplay
import com.cactusknights.chefbook.ui.themes.EncryptedDataTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeScreen(
    recipeId: String,
    defaultTab: RecipeScreenTab,
    recipeViewModel: RecipeScreenViewModel = hiltViewModel(),
    navController: NavHostController,
    sheetState: ModalBottomSheetState,
) {
    val context = LocalContext.current
    val resources = context.resources

    val recipeState = recipeViewModel.state.collectAsState()

    EncryptedDataTheme(
        isEncrypted = (recipeState.value as? RecipeScreenState.Success)?.recipe?.encryptionState is EncryptionState.Decrypted
    ) {
        RecipeEncryptionProvider(
            encryption = (recipeState.value as? RecipeScreenState.Success)?.recipe?.encryptionState ?: EncryptionState.Standard
        ) {
            RecipeScreenDisplay(
                state = recipeState.value,
                defaultTab = defaultTab,
                sheetState = sheetState,
                onEvent = { event -> recipeViewModel.obtainEvent(event) },
                onRefresh = { recipeViewModel.obtainEvent(RecipeScreenEvent.LoadRecipe(recipeId)) },
            )
        }
    }

    LaunchedEffect(Unit) {
        recipeViewModel.obtainEvent(RecipeScreenEvent.LoadRecipe(recipeId))
        recipeViewModel.effect.collect { effect ->
            when (effect) {
                is RecipeScreenEffect.EditRecipe -> {
                    navController.navigate(Destination.RecipeInput.route(recipeId))
                }
                is RecipeScreenEffect.Toast -> {
                    Toast.makeText(context, resources.getString(effect.messageId), Toast.LENGTH_SHORT).show()
                }
                is RecipeScreenEffect.ScreenClosed -> {
                    effect.messageId?.let { messageId ->
                        Toast.makeText(context, resources.getString(messageId), Toast.LENGTH_SHORT).show()
                    }
                    sheetState.hide()
                }
                is RecipeScreenEffect.CategoryScreenOpened -> {
                    val nextRoute = Destination.Home.RecipeBook.Category.route
                    val previousRoute = navController.previousBackStackEntry?.destination?.route
                    val previousRouteId = navController.previousBackStackEntry?.arguments?.getString(CATEGORY_ID_ARGUMENT)
                    if (nextRoute != previousRoute || effect.categoryId != previousRouteId) {
                        sheetState.hide()
                        navController.navigate(Destination.Home.RecipeBook.Category.route(effect.categoryId))
                    } else {
                        sheetState.hide()
                    }

                }
            }
        }
    }

}
