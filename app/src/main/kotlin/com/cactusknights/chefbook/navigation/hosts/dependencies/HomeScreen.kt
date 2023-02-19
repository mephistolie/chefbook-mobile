package com.cactusknights.chefbook.navigation.hosts.dependencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cactusknights.chefbook.navigation.graphs.HomeScreenNavGraph
import com.cactusknights.chefbook.navigation.navigators.AppNavigator
import com.mysty.chefbook.features.home.ui.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency

@Composable
fun DependenciesContainerBuilder<*>.HomeScreenDependencies(
  navigator: AppNavigator,
) {
    dependency(HomeScreenDestination) {
        remember { HomeScreenNavGraph(navigator = navigator) }
    }
}
