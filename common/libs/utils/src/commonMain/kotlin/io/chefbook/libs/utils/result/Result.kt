package io.chefbook.libs.utils.result

typealias EmptyResult = Result<Unit>

val successResult = Result.success(Unit)

fun <T> Result<T>.asEmpty(): Result<Unit> = map {}

inline fun <T, R> Result<T>.guard(action: (Result<R>) -> Unit): T {
  exceptionOrNull()?.let { action(Result.failure(it)) }
  return getOrThrow()
}

suspend inline fun <T> Result<T>.onSuccess(action: suspend (T) -> Unit): Result<T> {
  if (isSuccess) action(getOrThrow())
  return this
}

suspend inline fun <T> Result<T>.onFailure(action: suspend (exception: Throwable) -> Unit): Result<T> {
  exceptionOrNull()?.let { action(it) }
  return this
}

fun <T, R> Result<List<T>>.withListCast(cast: (T) -> R): Result<List<R>> = fold(
  onSuccess = { Result.success(it.map(cast)) },
  onFailure = { e -> Result.failure(e) }
)
