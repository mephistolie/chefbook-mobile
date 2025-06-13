package io.chefbook.libs.utils.url

import platform.Foundation.NSURL

actual fun isValidUrl(url: String): Boolean {
  val nsUrl = NSURL(string = url)
  return nsUrl.scheme != null && nsUrl.host != null
}
