package io.chefbook.sdk.settings.impl.data.platform

import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon

interface IconSwitcher {

  fun switchIconVisibility(icon: AppIcon, isEnabled: Boolean)
}
