package io.chefbook.libs.utils.interop

import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFMutableDictionaryRef
import platform.CoreFoundation.CFTypeRef

fun mutableDictOf(vararg pairs: Pair<CFTypeRef?, CFTypeRef?>): CFMutableDictionaryRef? {
  val parameters = CFDictionaryCreateMutable(null, pairs.size.toLong(), null, null)

  if (parameters != null) {
    pairs.forEach { (key, value) -> parameters[key] = value }
  }

  return parameters
}

operator fun CFMutableDictionaryRef.set(
  key: CFTypeRef?,
  value: CFTypeRef?,
) {
  CFDictionaryAddValue(this, key, value)
}
