package io.chefbook.libs.utils.uuid

import java.util.UUID

actual fun generateUUID() =
  UUID.randomUUID().toString()
