package io.chefbook.sdk.network.impl.clients.configuration

import io.chefbook.libs.utils.language.getSystemLanguageCode
import io.ktor.client.plugins.UserAgentConfig
import platform.UIKit.UIDevice
import platform.Foundation.NSBundle

internal fun UserAgentConfig.configureIosAgent() {
  val version = NSBundle.mainBundle().infoDictionary?.get("CFBundleShortVersionString")
  val buildNumber = NSBundle.mainBundle().infoDictionary?.get("CFBundleVersion")

  val model = UIDevice.currentDevice.model
  val iosVersion = UIDevice.currentDevice.systemVersion

  val languageCode = getSystemLanguageCode()

  agent =
    "ChefBook/$version (Darwin; U; iOS $iosVersion; $languageCode; $model; Build/$buildNumber)"
}
