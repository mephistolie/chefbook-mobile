package io.chefbook.libs.utils.interop

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.memScoped
import platform.Foundation.NSError
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFErrorRefVar

fun <T : Any> MemScope.objCTry(
  block: (error: CPointer<CFErrorRefVar>) -> T?,
): T {
  val errorH = alloc<CFErrorRefVar>()
  return when (val result = block(errorH.ptr)) {
    null -> error("Exception thrown: ${errorH.descriptionReleasing ?: "Unknown exception"}")
    else -> result
  }
}

fun <T : Any> swiftTry(
  block: (error: CPointer<ObjCObjectVar<NSError?>>) -> T?,
): T = memScoped {
  val errorH = alloc<ObjCObjectVar<NSError?>>()
  when (val result = block(errorH.ptr)) {
    null -> error("Exception thrown: ${errorH.value?.localizedDescription ?: "Unknown exception"}")
    else -> result
  }
}
