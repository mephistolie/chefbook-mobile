package io.chefbook.features.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.chefbook.features.root.component.RootComponent
import io.chefbook.features.root.ui.mvi.RootSideEffect
import io.chefbook.libs.logger.Logger
import io.chefbook.ui.utils.compose.composables.LaunchedEffect

@Composable
fun RootScreen(
  component: RootComponent,
) {
  LaunchedEffect {
    Logger.e { "subscribed" }
    component.store.sideEffectsFlow.collect { effect ->
      when (effect) {
        is RootSideEffect.SignedIn -> component.onSignedIn(profileId = effect.profileId)
        is RootSideEffect.SignedOut -> component.onSignedOut()
      }
    }
  }

//  setSingletonImageLoaderFactory {
//    ImageLoader.Builder(context)
//        .okHttpClient(get<OkHttpClient>())
//      .build()
//  }

  val state = component.store.stateFlow.collectAsState()
  RootScreenContent(
    state = state.value,
    component = component,
  )
}
