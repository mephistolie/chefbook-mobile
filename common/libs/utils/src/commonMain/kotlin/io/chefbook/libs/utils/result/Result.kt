package io.chefbook.libs.utils.result

typealias EmptyResult = Result<Unit>

val successResult = Result.success(Unit)

fun <T> Result<T>.asEmpty(): Result<Unit> =
  withCast {}

suspend inline fun <T> Result<T>.onSuccess(action: suspend (T) -> Unit): Result<T> {
  if (isSuccess) action(getOrThrow())
  return this
}

suspend inline fun <T> Result<T>.onFailure(action: suspend () -> Unit): Result<T> {
  if (isFailure) action()
  return this
}

fun <T, R> Result<T>.withCast(cast: (T) -> R): Result<R> = fold(
  onSuccess = { Result.success(cast(it)) },
  onFailure = { e -> Result.failure(e) }
)

fun <T, R> Result<List<T>>.withListCast(cast: (T) -> R): Result<List<R>> = fold(
  onSuccess = { Result.success(it.map(cast)) },
  onFailure = { e -> Result.failure(e) }
)
