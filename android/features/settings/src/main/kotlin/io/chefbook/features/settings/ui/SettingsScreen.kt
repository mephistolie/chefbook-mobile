package io.chefbook.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.settings.navigation.SettingsScreenNavigator
import io.chefbook.features.settings.ui.mvi.SettingsScreenEffect
import org.koin.androidx.compose.koinViewModel

@Destination(route = "settings")
@Composable
fun SettingsScreen(
  navigator: SettingsScreenNavigator
) {
  val viewModel: ISettingsScreenViewModel = koinViewModel<SettingsScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  SettingsScreenContent(
    state = state.value.settings,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is SettingsScreenEffect.Closed -> navigator.navigateUp()
        is SettingsScreenEffect.AppRestarted -> navigator.restartApp()
      }
    }
  }
}
