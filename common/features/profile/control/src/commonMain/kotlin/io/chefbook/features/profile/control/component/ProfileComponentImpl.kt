package io.chefbook.features.profile.control.component

import com.arkivanov.decompose.ComponentContext
import io.chefbook.features.profile.control.mvi.ProfileStore
import io.chefbook.libs.mvi.decompose.retainedStore
import org.koin.core.component.KoinScopeComponent

class ProfileComponentImpl(
  componentContext: ComponentContext,
  profileScope: KoinScopeComponent,
) : ProfileComponent, ComponentContext by componentContext {

  override val store: ProfileStore by retainedStore(profileScope)
}
