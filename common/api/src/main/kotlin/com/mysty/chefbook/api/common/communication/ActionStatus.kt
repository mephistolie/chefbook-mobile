package com.mysty.chefbook.api.common.communication

sealed class ActionStatus<out T> {

    object Loading : ActionStatus<Nothing>()

    sealed class Success<T> : ActionStatus<T>() {

        abstract val data: T

        class Data<T>(override val data: T) : Success<T>()

        object Empty : Success<Nothing>() {

            override val data: Nothing get() = error("No value")

        }
    }

    data class Failure(
        val error: Throwable? = null
    ) : ActionStatus<Nothing>()

}

fun <T> ActionStatus<T>.isSuccess(): Boolean = this is ActionStatus.Success

fun <T> ActionStatus<T>.isFailure(): Boolean = this is ActionStatus.Failure

fun <T> ActionStatus<T>.asSuccess(): ActionStatus.Success<T> = this as ActionStatus.Success<T>

fun <T> ActionStatus<T>.data(): T = this.asSuccess().data

fun <T> ActionStatus<T>.safeData(): T? = (this as? ActionStatus.Success)?.data

fun <T> ActionStatus<T>.asFailure(): ActionStatus.Failure = this as ActionStatus.Failure

fun <T> ActionStatus<T>.letIfSuccess(block: (T) -> Unit) = run {
    if (this.isSuccess()) {
        block(this.data())
    }
}

fun <T> ActionStatus<T>.letIfFailure(block: (Throwable?) -> Unit) = run {
    if (this.isFailure()) {
        block(this.asFailure().error)
    }
}

fun <T> ActionStatus<T>.asEmpty(): SimpleAction =
    if (this.isSuccess()) SuccessResult else this.asFailure()

typealias Loading = ActionStatus.Loading
typealias SimpleAction = ActionStatus<Nothing>
typealias Successful<T> = ActionStatus.Success<T>
typealias SuccessResult = ActionStatus.Success.Empty
typealias DataResult<T> = ActionStatus.Success.Data<T>
typealias Failure = ActionStatus.Failure
