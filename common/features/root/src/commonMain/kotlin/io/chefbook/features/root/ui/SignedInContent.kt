package io.chefbook.features.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.chefbook.features.profile.control.ui.ProfileScreen
import io.chefbook.features.root.component.SignedInComponent
import org.koin.compose.LocalKoinScope

@Composable
fun SignedInContent(
  component: SignedInComponent,
  modifier: Modifier = Modifier,
) {
  CompositionLocalProvider(
    LocalKoinScope provides component.scope,
  ) {
    Children(
      stack = component.children,
      modifier = modifier,
      animation = stackAnimation(slide()),
    ) { child ->
      when (val instance = child.instance) {
        is SignedInComponent.Child.Profile -> ProfileScreen(component = instance.component)
      }
    }
  }
}
