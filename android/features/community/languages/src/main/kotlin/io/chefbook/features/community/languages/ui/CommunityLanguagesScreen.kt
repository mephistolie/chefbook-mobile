package io.chefbook.features.community.languages.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.community.languages.ui.mvi.CommunityLanguagesScreenEffect
import io.chefbook.navigation.navigators.BaseNavigator
import org.koin.androidx.compose.getViewModel

@Destination(
  route = "community/languages",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun CommunityLanguagesScreen(
  navigator: BaseNavigator,
) {
  val viewModel = getViewModel<CommunityLanguagesScreenViewModel>()
  val state = viewModel.state.collectAsState()

  CommunityLanguagesScreenContent(
    selectedLanguages = state.value.languages,
    onIntent = { intent -> viewModel.handleIntent(intent) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        CommunityLanguagesScreenEffect.Back -> navigator.navigateUp()
      }
    }
  }
}
