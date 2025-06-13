package io.chefbook.sdk.settings.impl.di

import io.chefbook.sdk.settings.impl.data.platform.IconSwitcher
import org.koin.core.scope.Scope

expect fun Scope.iconSwitcher(): IconSwitcher
