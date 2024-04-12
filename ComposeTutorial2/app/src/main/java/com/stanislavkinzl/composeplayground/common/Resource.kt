package com.stanislavkinzl.composeplayground.common

import com.stanislavkinzl.composeplayground.common.APIErrorType.*
import com.stanislavkinzl.composeplayground.isEmptyList
import com.stanislavkinzl.composeplayground.isNullOrEmptyList

/** A generic class that contains data and status about loading this data. */
sealed class Resource<T>(
    open val data: T? = null,
    open val message: String? = null,
    open val exception: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    data class Error<T>(
        override val message: String? = null,
        override val exception: Exception? = null,
        override val data: T? = null,
        val errorType: APIErrorType
    ) : Resource<T>(data, message, exception)
    class Other<T>(data: T? = null) : Resource<T>(data)
}

enum class APIErrorType {
    NotConnected,
    Backend,
    Generic
}

fun <T> Resource<T>.toUiState(): UiState<T> {
    return when (this) {
        is Resource.Error -> {
            data?.let {
                if (it.isEmptyList()) { // initial data might be empty from Room,
                    // in that case don't map to Content, rather to pure error ui state
                    toPureErrorUiState()
                } else {
                    UiState.Content(
                        data = it,
                        contentErrorMode = ContentErrorMode(
                            errorType = this.errorType,
                            message = this.message.orEmpty()
                        )
                    )
                }
            } ?: run {
                toPureErrorUiState()
            }
        }
        is Resource.Loading -> {
            this.data?.let {
                UiState.Content(it, loadingMode = true)
            } ?: run {
                UiState.FullscreenLoading()
            }
        }
        is Resource.Other -> UiState.Other()
        is Resource.Success -> {
            data?.let {
                if (it.isEmptyList()) {
                    UiState.Empty()
                } else {
                    UiState.Content(it)
                }
            } ?: run {
                UiState.Empty()
            }
        }
    }
}

private fun <T> Resource.Error<T>.toPureErrorUiState(): UiState<T> {
    return when (this.errorType) {
        NotConnected -> UiState.NotConnected()
        Backend -> UiState.ServerError(message = message)
        Generic -> UiState.GenericError(message = message)
    }
}