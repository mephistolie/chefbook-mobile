package io.chefbook.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import io.chefbook.features.root.RootComponent
import io.chefbook.ui.screens.main.mvi.RootEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootScreen(
  component: RootComponent,
) {
  val viewModel = koinViewModel<RootViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  val context = LocalPlatformContext.current

//  setSingletonImageLoaderFactory {
//    ImageLoader.Builder(context)
//        .okHttpClient(get<OkHttpClient>())
//      .build()
//  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RootEffect.SignedIn -> component.onSignedIn(profileId = effect.profileId)
        is RootEffect.SignedOut -> component.onSignedOut()
      }
    }
  }

  RootScreenContent(
    state = state.value,
    component = component,
  )
}
