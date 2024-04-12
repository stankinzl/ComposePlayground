package com.stanislavkinzl.composeplayground.common

sealed class UiState<T> {

    data class Content<T>(
        val data: T,
        val contentErrorMode: ContentErrorMode? = null, // For when wanting to show fe. old/offline content, but new requests return errors
        val loadingMode: Boolean = false // For when there is data and lo
    ) : UiState<T>()

    class NotConnected<T> : UiState<T>()
    data class ServerError<T>(val message: String? = null) : UiState<T>()
    data class GenericError<T>(val message: String? = null) : UiState<T>()
    class Other<T> : UiState<T>()
    class Empty<T> : UiState<T>()
    class FullscreenLoading<T> : UiState<T>()
}

data class ContentErrorMode(
    val errorType: APIErrorType,
    val message: String
)