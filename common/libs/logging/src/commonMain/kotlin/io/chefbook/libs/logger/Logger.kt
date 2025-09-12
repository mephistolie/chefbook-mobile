package io.chefbook.libs.logger

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object Logger {

  fun initDebug() {
    Napier.base(DebugAntilog(defaultTag = "ChefBook"))
  }

  inline fun v(throwable: Throwable? = null, tag: String? = null, noinline message: () -> String) =
    Napier.v(throwable = throwable, tag = tag, message = message)

  inline fun d(throwable: Throwable? = null, tag: String? = null, noinline message: () -> String) =
    Napier.d(throwable = throwable, tag = tag, message = message)

  inline fun i(throwable: Throwable? = null, tag: String? = null, noinline message: () -> String) =
    Napier.i(throwable = throwable, tag = tag, message = message)

  inline fun w(throwable: Throwable? = null, tag: String? = null, noinline message: () -> String) =
    Napier.w(throwable = throwable, tag = tag, message = message)

  inline fun e(throwable: Throwable? = null, tag: String? = null, noinline message: () -> String) =
    Napier.e(throwable = throwable, tag = tag, message = message)

  inline fun wtf(
    throwable: Throwable? = null,
    tag: String? = null,
    noinline message: () -> String
  ) =
    Napier.wtf(throwable = throwable, tag = tag, message = message)
}
