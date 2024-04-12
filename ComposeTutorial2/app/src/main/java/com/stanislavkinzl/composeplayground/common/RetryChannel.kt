package com.stanislavkinzl.composeplayground.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.experimental.ExperimentalTypeInference

class RetryChannel private constructor(private val channel: Channel<Unit>) {
    suspend fun retry() { channel.send(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTypeInference::class)
    fun <R> flatMapLatest(@BuilderInference transform: suspend (value: Unit) -> Flow<R>): Flow<R> {
        return channel
            .receiveAsFlow()
            .onStart { emit(Unit) }
            .flatMapLatest(transform)
    }

    companion object {
        operator fun invoke() = RetryChannel(Channel(Channel.CONFLATED))
    }
}