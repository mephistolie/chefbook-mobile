package io.chefbook.libs.utils.time

import platform.CoreFoundation.CFAbsoluteTimeGetCurrent

actual fun currentTimeMillis(): Long =
  CFAbsoluteTimeGetCurrent().toLong()
