package io.chefbook.libs.utils.uuid

import platform.Foundation.NSUUID

actual fun generateUUID() =
  NSUUID().UUIDString
