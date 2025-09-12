package io.chefbook.features.auth.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.chefbook.features.auth.AuthComponent
import io.chefbook.ui.utils.commonGeneralEula
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.components.texts.HyperlinkText
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenAgreement
import io.chefbook.features.auth.signup.activation.ui.ProfileActivationForm
import io.chefbook.features.auth.profiles.ui.ProfilesListForm
import io.chefbook.features.auth.signin.login.ui.SignInLoginForm
import io.chefbook.features.auth.signin.password.ui.SignInPasswordForm
import io.chefbook.features.auth.signup.email.ui.SignUpEmailForm
import io.chefbook.features.auth.signup.password.ui.SignUpPasswordForm
import io.chefbook.features.auth.ui.components.ChefBookLogo
import org.jetbrains.compose.resources.stringResource
import io.chefbook.ui.utils.Res as CoreR

@Composable
fun AuthScreen(
  component: AuthComponent, modifier: Modifier = Modifier
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Box(
    modifier = modifier.fillMaxSize().background(colors.backgroundPrimary).systemBarsPadding(),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .align(Alignment.TopCenter)
        .verticalScroll(state = rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(modifier = Modifier.height(128.dp))
      ChefBookLogo()
      Spacer(modifier = Modifier.height(20.dp))
      Children(
        stack = component.children,
        animation = stackAnimation(
          animator = slide(),
        ),
      ) { child ->
        Column(
          modifier = modifier
            .imePadding()
            .padding(horizontal = 48.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          when (val child = child.instance) {
            is AuthComponent.Child.ProfilesList -> ProfilesListForm(component = child.component)
            is AuthComponent.Child.SignInLogin -> SignInLoginForm(component = child.component)
            is AuthComponent.Child.SignInPassword -> SignInPasswordForm(component = child.component)
            is AuthComponent.Child.SignUpEmail -> SignUpEmailForm(component = child.component)
            is AuthComponent.Child.SignUpPassword -> SignUpPasswordForm(component = child.component)
            is AuthComponent.Child.ProfileActivation -> ProfileActivationForm(component = child.component)
            AuthComponent.Child.PasswordResetConfirmation -> TODO()
            AuthComponent.Child.PasswordResetForm -> TODO()
          }
        }
      }
    }

    HyperlinkText(
      text = stringResource(Res.string.commonAuthScreenAgreement),
      hyperlinks = listOf(stringResource(CoreR.string.commonGeneralEula) to "https://chefbook.io/eula"),
      modifier = Modifier.align(Alignment.BottomCenter).padding(
        start = 48.dp,
        end = 48.dp,
        bottom = 48.dp,
      ),
      style = typography.body2.copy(textAlign = TextAlign.Center),
    )
  }
}
