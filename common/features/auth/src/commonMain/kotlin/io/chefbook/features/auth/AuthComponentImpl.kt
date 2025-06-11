package io.chefbook.features.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import io.chefbook.features.auth.AuthComponent.Child
import io.chefbook.features.auth.login.component.LoginFormComponentImpl
import io.chefbook.features.auth.profiles.component.ProfilesListComponentImpl
import kotlinx.serialization.Serializable

class AuthComponentImpl(
  componentContext: ComponentContext,
) : ComponentContext by componentContext, AuthComponent {

  private val navigation = StackNavigation<Config>()

//  override val children: Value<ChildStack<*, Child>> =
//    childStack(
//      source = navigation,
//      serializer = Config.serializer(),
//      initialStack = { emptyList<Config>() },
//    ) { config, childComponentContext ->
//      when (config) {
//        Config.ProfilesList -> Child.ProfilesList(
//          component = ProfilesListComponentImpl(
//            componentContext = childComponentContext,
//            onProfileSelected = TODO(),
//            onAnotherProfileButtonClicked = ::onAnotherProfileButtonClicked,
//          )
//        )
//
//        Config.Login -> Child.Login(
//          component = LoginFormComponentImpl(
//            componentContext = childComponentContext,
//            onSignInButtonClicked = TODO(),
//            onProfilesListButtonClicked = ::onProfilesListButtonClicked,
//          ),
//        )
//
//        Config.PasswordResetConfirmationForm -> TODO()
//        Config.PasswordResetForm -> TODO()
//        Config.SignInPasswordForm -> TODO()
//      }
//    }

  private fun onProfilesListButtonClicked() {
    navigation.pushNew(Config.ProfilesList)
  }

  private fun onAnotherProfileButtonClicked() {
    navigation.pushNew(Config.Login)
  }

  @Serializable
  private sealed interface Config {

    @Serializable
    data object ProfilesList : Config

    @Serializable
    data object Login : Config

    @Serializable
    data object SignInPasswordForm : Config

    @Serializable
    data object PasswordResetForm : Config

    @Serializable
    data object PasswordResetConfirmationForm : Config
  }
}
