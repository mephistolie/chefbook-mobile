package io.chefbook.sdk.settings.impl.data.platform

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon

internal class IconSwitcherImpl(
  private val context: Context,
) : IconSwitcher {

  private val packageManager = context.packageManager

  override fun switchIconVisibility(icon: AppIcon, isEnabled: Boolean) =
    setComponent(classname = "$APP_ID.$icon", isEnabled = isEnabled)

  private fun setComponent(
    classname: String,
    isEnabled: Boolean,
  ) {
    packageManager.setComponentEnabledSetting(
      ComponentName(context, classname),
      if (isEnabled) {
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED
      } else {
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED
      },
      PackageManager.DONT_KILL_APP
    )
  }

  companion object {
    private const val APP_ID = "io.chefbook"
  }
}