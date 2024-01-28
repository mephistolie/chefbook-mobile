package io.chefbook.libs.utils.url

import android.webkit.URLUtil

actual fun isValidUrl(url: String): Boolean =
  URLUtil.isValidUrl(url)
