package io.chefbook.libs.logger

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object Logger {

  fun initDebug() {
    Napier.base(DebugAntilog(defaultTag = "ChefBook"))
  }

  fun v(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
    Napier.v(throwable = throwable, tag = tag, message = message)

  fun d(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
    Napier.d(throwable = throwable, tag = tag, message = message)

  fun i(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
    Napier.i(throwable = throwable, tag = tag, message = message)

  fun w(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
    Napier.w(throwable = throwable, tag = tag, message = message)

  fun e(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
    Napier.e(throwable = throwable, tag = tag, message = message)

  fun wtf(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
    Napier.wtf(throwable = throwable, tag = tag, message = message)
}
