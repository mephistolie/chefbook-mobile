package io.chefbook.navigation.hosts.dependencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import io.chefbook.navigation.graphs.NavGraphs

@Composable
fun DependenciesContainerBuilder<*>.RecipeScreenDependencies() {
  dependency(NavGraphs.recipe) {
    val parentEntry = remember(navBackStackEntry) {
      navController.getBackStackEntry(NavGraphs.recipe.route)
    }
    parentEntry.arguments?.getString(NavGraphs.RECIPE_ID_ARGUMENT)!!
  }
}
