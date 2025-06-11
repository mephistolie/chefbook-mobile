package io.chefbook.libs.utils.interop

import kotlinx.cinterop.value
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFTypeRef
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSError

inline fun <T : CFTypeRef?, R> T.withBridgingRelease(block: (T) -> R): R {
  try {
    return block(this)
  } finally {
    bridgingRelease()
  }
}

val CFErrorRefVar.descriptionReleasing get() = value.bridgingRelease<NSError>()?.description

@Suppress("UNCHECKED_CAST")
fun <T : Any> Any?.bridgingRetain(): T? = bridgingRetain()?.let { it as T }
fun Any?.bridgingRetain(): CFTypeRef? = CFBridgingRetain(this)

@Suppress("UNCHECKED_CAST")
fun <T : Any> CFTypeRef?.bridgingRelease(): T? = CFBridgingRelease(this)?.let { it as T }
fun CFTypeRef?.bridgingRelease(): Any? = CFBridgingRelease(this)
