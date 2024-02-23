package io.chefbook.navigation.hosts.dependencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import io.chefbook.features.community.recipes.ui.viewmodel.CommunityRecipesScreenViewModel
import io.chefbook.navigation.graphs.NavGraphs
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DependenciesContainerBuilder<*>.CommunityRecipesScreen() {
  dependency(NavGraphs.communityRecipes) {
    val parentEntry = remember(navBackStackEntry) {
      navController.getBackStackEntry(NavGraphs.communityRecipes.route)
    }
    val initialSearch = parentEntry.arguments?.getString(NavGraphs.SEARCH_ARGUMENT)
    koinViewModel<CommunityRecipesScreenViewModel>(viewModelStoreOwner = parentEntry) {
      parametersOf(initialSearch.orEmpty())
    }
  }
}
