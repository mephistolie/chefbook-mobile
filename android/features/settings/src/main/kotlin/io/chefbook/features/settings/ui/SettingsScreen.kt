package io.chefbook.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import io.chefbook.features.settings.ui.mvi.SettingsScreenEffect
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.settings.navigation.SettingsScreenNavigator
import org.koin.androidx.compose.getViewModel

@Destination(route = "settings")
@Composable
fun SettingsScreen(
  navigator: SettingsScreenNavigator
) {
  val viewModel: ISettingsScreenViewModel = getViewModel<SettingsScreenViewModel>()
  val state = viewModel.state.collectAsState()

  val context = LocalContext.current

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
