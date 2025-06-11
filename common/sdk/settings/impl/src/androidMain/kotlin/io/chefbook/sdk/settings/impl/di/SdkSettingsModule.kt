package io.chefbook.sdk.settings.impl.di

import io.chefbook.sdk.settings.impl.data.platform.IconSwitcher
import io.chefbook.sdk.settings.impl.data.platform.IconSwitcherImpl
import org.koin.core.scope.Scope

actual fun Scope.iconSwitcher(): IconSwitcher =
  IconSwitcherImpl(context = get())
