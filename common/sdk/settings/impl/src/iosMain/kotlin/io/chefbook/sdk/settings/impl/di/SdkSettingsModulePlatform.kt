package io.chefbook.sdk.settings.impl.di

import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.impl.data.platform.IconSwitcher
import org.koin.core.scope.Scope

// TODO: Icon Switching
actual fun Scope.iconSwitcher(): IconSwitcher =
  object : IconSwitcher {
    override fun switchIconVisibility(icon: AppIcon, isEnabled: Boolean) = Unit
  }
