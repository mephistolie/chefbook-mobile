package io.chefbook.libs.logger

actual object Logger {

  actual fun v(message: String?, vararg args: Any?) = Unit

  actual fun v(t: Throwable?, message: String?, vararg args: Any?) = Unit

  actual fun v(t: Throwable?) = Unit

  actual fun d(message: String?, vararg args: Any?) = Unit

  actual fun d(t: Throwable?, message: String?, vararg args: Any?) = Unit

  actual fun d(t: Throwable?) = Unit

  actual fun i(message: String?, vararg args: Any?) = Unit

  actual fun i(t: Throwable?, message: String?, vararg args: Any?) = Unit

  actual fun i(t: Throwable?) = Unit

  actual fun w(message: String?, vararg args: Any?) = Unit

  actual fun w(t: Throwable?, message: String?, vararg args: Any?) = Unit

  actual fun w(t: Throwable?) = Unit

  actual fun e(message: String?, vararg args: Any?) = Unit

  actual fun e(t: Throwable?, message: String?, vararg args: Any?) = Unit

  actual fun e(t: Throwable?) = Unit
}
