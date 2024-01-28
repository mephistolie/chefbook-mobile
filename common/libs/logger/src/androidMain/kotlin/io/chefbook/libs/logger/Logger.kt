package io.chefbook.libs.logger

actual object Logger {

  fun plantDebug() = Timber.plant(Timber.DebugTree())

  actual fun v(message: String?, vararg args: Any?) = Timber.v(message, args)

  actual fun v(t: Throwable?, message: String?, vararg args: Any?) = Timber.v(t, message, args)

  actual fun v(t: Throwable?) = Timber.v(t)

  actual fun d(message: String?, vararg args: Any?) = Timber.d(message, args)

  actual fun d(t: Throwable?, message: String?, vararg args: Any?) = Timber.d(t, message, args)

  actual fun d(t: Throwable?) = Timber.d(t)

  actual fun i(message: String?, vararg args: Any?) = Timber.i(message, args)

  actual fun i(t: Throwable?, message: String?, vararg args: Any?) = Timber.i(t, message, args)

  actual fun i(t: Throwable?) = Timber.i(t)

  actual fun w(message: String?, vararg args: Any?) = Timber.w(message, args)

  actual fun w(t: Throwable?, message: String?, vararg args: Any?) = Timber.w(t, message, args)

  actual fun w(t: Throwable?) = Timber.w(t)

  actual fun e(message: String?, vararg args: Any?) = Timber.e(message, args)

  actual fun e(t: Throwable?, message: String?, vararg args: Any?) = Timber.e(t, message, args)

  actual fun e(t: Throwable?) = Timber.e(t)
}
