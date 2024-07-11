package com.thit.firebaseauthentication.data.domain.model

sealed class SafeResult<out T> {

    data class Success<T>(val data: T) : SafeResult<T>()
    data class Error(
        val exception: Exception? = Exception("Unknown Error"),
        val message: String = exception?.localizedMessage ?: ""
    ) : SafeResult<Nothing>()

    object Loading : SafeResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Failure[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}


/**
 * `true` if [SafeResult] is of type [Success] & holds non-null [Success.data].
 */
val SafeResult<*>.succeeded
    get() = this is SafeResult.Success && data != null

fun <T> SafeResult<T>.getSuccessOrNull(): T? {
    return when (this) {
        is SafeResult.Success -> this.data
        else -> null
    }
}

fun <T> SafeResult<T>.getErrorOrNull(): SafeResult.Error? {
    return when (this) {
        is SafeResult.Error -> this
        else -> null
    }
}