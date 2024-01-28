package io.chefbook.navigation.hosts.dependencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import io.chefbook.navigation.graphs.NavGraphs
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
