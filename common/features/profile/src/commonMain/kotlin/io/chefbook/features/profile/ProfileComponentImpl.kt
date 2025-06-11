package io.chefbook.features.profile

import com.arkivanov.decompose.ComponentContext

class ProfileComponentImpl(
  componentContext: ComponentContext,
) : ProfileComponent, ComponentContext by componentContext
