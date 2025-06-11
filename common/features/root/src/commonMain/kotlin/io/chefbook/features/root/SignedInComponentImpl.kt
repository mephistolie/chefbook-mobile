package io.chefbook.features.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import io.chefbook.features.profile.ProfileComponentImpl
import io.chefbook.libs.di.scopes.ProfileComponent
import kotlinx.serialization.Serializable
import org.koin.core.scope.Scope

class SignedInComponentImpl(
  private val profileId: String,
  private val onSignedOut: () -> Unit,
  componentContext: ComponentContext,
) : SignedInComponent, ComponentContext by componentContext {

  val diComponent by lazy { ProfileComponent(profileId) }

  override val scope: Scope by lazy { diComponent.scope }

  private val navigation = StackNavigation<Config>()

  override val children: Value<ChildStack<*, SignedInComponent.Child>> =
    childStack(
      source = navigation,
      serializer = Config.serializer(),
      initialConfiguration = Config.Profile,
    ) { config, childComponentContext ->
      when (config) {
        Config.Profile -> SignedInComponent.Child.Profile(
          component = ProfileComponentImpl(childComponentContext)
        )
      }
    }

  init {
    lifecycle.doOnDestroy(diComponent::close)
  }

  @Serializable
  sealed interface Config {

    @Serializable
    data object Profile : Config
  }
}
