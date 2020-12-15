package org.arhor.photospot.accountanttelegrambot.core

@Suppress("unused")
sealed class ActionResult<out T> {

    abstract val isSuccess: Boolean
    abstract val isFailure: Boolean

    data class Success<out T>(val value: T? = null) : ActionResult<T>() {
        override val isSuccess = true
        override val isFailure = false
    }

    data class Failure(val error: Throwable) : ActionResult<Nothing>() {
        override val isSuccess = false
        override val isFailure = true
    }

    override fun toString(): String {
        return when (this) {
            is Success -> value?.toString()
                ?: "success"
            is Failure -> error.message
                ?: "failure"
        }
    }

    inline fun <R> map(f: (T?) -> R): ActionResult<R> {
        return when (this) {
            is Success<T> -> actionResult { f(value) }
            is Failure -> this
        }
    }

    inline fun <R> flatMap(f: (T?) -> ActionResult<R>): ActionResult<R> {
        return when (this) {
            is Success<T> -> f(value)
            is Failure -> this
        }
    }

    inline fun catch(handler: (Throwable) -> Unit) {

        if (this is Failure) {
            handler(error)
        }
    }

    companion object {
        fun success(): Success<Nothing> = Success()

        fun <T> success(value: T?): Success<T> = Success(value)

        fun failure(error: Throwable): Failure = Failure(error)

        fun failure(message: String): Failure = Failure(RuntimeException(message))

        inline fun <T> actionResult(action: () -> T?): ActionResult<T> {
            return try {
                Success(action())
            } catch (error: Throwable) {
                Failure(error)
            }
        }
    }
}
