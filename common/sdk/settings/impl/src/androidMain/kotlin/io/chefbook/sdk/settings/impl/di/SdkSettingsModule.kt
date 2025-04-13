package io.chefbook.sdk.settings.impl.di

import io.chefbook.sdk.settings.impl.data.platform.IconSwitcher
import io.chefbook.sdk.settings.impl.data.platform.IconSwitcherImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun iconSwitcherModule() = module {

  singleOf(::IconSwitcherImpl) bind IconSwitcher::class
}
