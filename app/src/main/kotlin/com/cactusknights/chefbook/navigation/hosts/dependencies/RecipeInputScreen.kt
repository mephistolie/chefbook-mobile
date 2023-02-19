package com.cactusknights.chefbook.navigation.hosts.dependencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cactusknights.chefbook.navigation.graphs.NavGraphs
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DependenciesContainerBuilder<*>.RecipeInputScreenDependencies() {
    dependency(NavGraphs.recipeInput) {
        val parentEntry = remember(navBackStackEntry) {
            navController.getBackStackEntry(NavGraphs.recipeInput.route)
        }
        val recipeId = parentEntry.arguments?.getString(NavGraphs.RECIPE_ID_ARGUMENT)
        getViewModel<RecipeInputScreenViewModel>(viewModelStoreOwner = parentEntry) {
            parametersOf(recipeId)
        }
    }
}
