package io.chefbook.features.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import io.chefbook.features.auth.AuthComponentImpl
import kotlinx.serialization.Serializable

class RootComponentImpl(
  componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

  private val navigation = SlotNavigation<Config>()

  override val child: Value<ChildSlot<*, RootComponent.Child>> =
    childSlot(
      source = navigation,
      serializer = Config.serializer(),
    ) { config, childComponentContext ->
      when (config) {
        Config.SignedOut -> RootComponent.Child.SignedOut(
          component = AuthComponentImpl(
            componentContext = childComponentContext,
          )
        )

        is Config.SignedIn -> RootComponent.Child.SignedIn(
          component = SignedInComponentImpl(
            profileId = config.profileId,
            componentContext = childComponentContext,
            onSignedOut = ::onSignedOut,
          )
        )
      }
    }

  override fun onSignedIn(profileId: String) {
    navigation.activate(Config.SignedIn(profileId))
  }

  override fun onSignedOut() {
    navigation.activate(Config.SignedOut)
  }

  @Serializable
  private sealed interface Config {

    @Serializable
    data object SignedOut : Config

    @Serializable
    data class SignedIn(val profileId: String) : Config
  }
}
