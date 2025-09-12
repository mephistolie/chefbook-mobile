package io.chefbook.features.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import io.chefbook.features.auth.AuthComponent.Child
import io.chefbook.features.auth.AuthComponent.Child.*
import io.chefbook.features.auth.profiles.component.ProfilesListComponentImpl
import io.chefbook.features.auth.signin.login.component.SignInLoginComponentImpl
import io.chefbook.features.auth.signin.password.component.SignInPasswordComponentImpl
import io.chefbook.features.auth.signup.activation.component.ProfileActivationComponentImpl
import io.chefbook.features.auth.signup.email.component.SignUpEmailComponentImpl
import io.chefbook.features.auth.signup.password.component.SignUpPasswordComponentImpl
import kotlinx.serialization.Serializable

class AuthComponentImpl(
  componentContext: ComponentContext,
) : ComponentContext by componentContext, AuthComponent {

  private val navigation = StackNavigation<Config>()

  override val children: Value<ChildStack<*, Child>> =
    childStack(
      source = navigation,
      serializer = Config.serializer(),
      initialConfiguration = Config.ProfilesList,
      handleBackButton = true,
    ) { config, childComponentContext ->
      when (config) {
        Config.ProfilesList -> ProfilesList(
          component = ProfilesListComponentImpl(
            componentContext = childComponentContext,
            onProfileSelected = { TODO() },
            onAnotherProfileButtonClicked = { openSignInLogin(initialLogin = "") },
          ),
        )

        Config.SignInLogin -> SignInLogin(
          component = SignInLoginComponentImpl(
            componentContext = childComponentContext,
            onProfileListButtonClicked = ::openProfilesList,
            onSignInButtonClicked = ::openSignInPassword,
            onSignUpButtonClicked = ::openSignUpEmail,
          ),
        )

        is Config.SignInPassword -> SignInPassword(
          component = SignInPasswordComponentImpl(
            componentContext = childComponentContext,
            login = config.login,
            onBackButtonClicked = { navigation.pop() },
            onResetPasswordClicked = { openPasswordReset(config.login) },
          ),
        )

        is Config.SignUpEmail -> SignUpEmail(
          component = SignUpEmailComponentImpl(
            componentContext = childComponentContext,
            onProfileListButtonClicked = ::openProfilesList,
            onNextButtonClicked = ::openSignUpPassword,
            onSignInButtonClicked = ::openSignInLogin,
          ),
        )

        is Config.SignUpPassword -> SignUpPassword(
          component = SignUpPasswordComponentImpl(
            componentContext = childComponentContext,
            login = config.login,
            onBackButtonClicked = { navigation.pop() },
            onProfileExists = { openSignInLogin(initialLogin = config.login) },
            onProfileBlocked = { openSignInLogin(initialLogin = "") },
          )
        )

        is Config.ProfileActivation -> ProfileActivation(
          component = ProfileActivationComponentImpl(
            componentContext = childComponentContext,
            profileId = config.profileId,
            email = config.email,
            onBackButtonClicked = { openSignUpEmail(initialEmail = config.email) },
            onProfileActivated = { openSignInLogin(initialLogin = config.email) }
          )
        )

        is Config.PasswordReset -> { TODO() }
        Config.PasswordResetConfirmation -> TODO()
      }
    }

  private fun openProfilesList() {
    navigation.replaceAll(Config.ProfilesList)
  }

  private fun openSignInLogin(initialLogin: String) {
    navigation.replaceAll(Config.SignInLogin) {
      val child = children.value.active.instance as? SignInLogin ?: return@replaceAll
      val component = child.component as? SignInLoginComponentImpl ?: return@replaceAll
      component.setLogin(initialLogin)
    }
  }

  private fun openSignInPassword(login: String) {
    navigation.pushNew(Config.SignInPassword(login))
  }

  private fun openSignUpEmail(initialEmail: String) {
    navigation.pushToFront(Config.SignUpEmail) {
      val child = children.value.active.instance as? SignUpEmail ?: return@pushToFront
      val component = child.component as? SignUpEmailComponentImpl ?: return@pushToFront
      component.setEmail(initialEmail)
    }
  }

  private fun openSignUpPassword(email: String) {
    navigation.pushNew(Config.SignUpPassword(email))
  }

  private fun openPasswordReset(login: String) {
    navigation.pushNew(Config.PasswordReset(login = login))
  }

  @Serializable
  private sealed interface Config {

    @Serializable
    data object ProfilesList : Config

    @Serializable
    data object SignInLogin : Config

    @Serializable
    data class SignInPassword(
      val login: String,
    ) : Config

    @Serializable
    data object SignUpEmail : Config

    @Serializable
    data class SignUpPassword(
      val login: String,
    ) : Config

    @Serializable
    data class ProfileActivation(
      val profileId: String,
      val email: String,
    ) : Config

    @Serializable
    data class PasswordReset(
      val login: String,
    ) : Config

    @Serializable
    data object PasswordResetConfirmation : Config
  }
}
