package com.stanislavkinzl.composeplayground.repository

import com.stanislavkinzl.composeplayground.common.APIErrorType
import com.stanislavkinzl.composeplayground.common.Resource
import com.stanislavkinzl.composeplayground.isNullOrEmptyList
import com.stanislavkinzl.composeplayground.network.helpers.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.net.SocketTimeoutException

// Deep dive into dispatchers: https://medium.com/@bhavnathacker14/deep-dive-into-dispatchers-for-kotlin-coroutines-f38527bde94c
// Best practises for coroutines: https://developer.android.com/kotlin/coroutines/coroutines-best-practices
/**
 * @param initialData Either returns Error with starting data of APIErrorType.NotConnected or with Loading if connected
 * */
fun <D, R> apiFlow(
    networkMonitor: NetworkMonitor,
    apiOperation: suspend () -> R,
    successMappingOperation: suspend (R) -> D,
    initialData: (suspend () -> D?)? = null, // Either returns Error with starting data of APIErrorType.NotConnected
    withMappedData: (suspend (D) -> Unit)? = null
): Flow<Resource<D>> {
    var startingData: D? = null
    return flow<Resource<D>> {
        startingData = initialData?.invoke()
        if (networkMonitor.isConnected.firstOrNull() != true) {
            emit(
                Resource.Error(
                    data = startingData,
                    errorType = APIErrorType.NotConnected,
                    message = "No internet connection available"
                )
            )
            return@flow // Not connected. Return.
        }
        startingData?.let {
            if (it is List<*> && it.isNullOrEmptyList()) {
                emit(Resource.Loading(data = null))
            } else {
                emit(Resource.Loading(data = it))
            }
        } ?: run {
            emit(Resource.Loading())
        }
        val response = apiOperation.invoke()
        val successData = successMappingOperation.invoke(response)
        withMappedData?.invoke(successData)
        emit(Resource.Success(successData))
    }.catch { e ->
        e.printStackTrace()
        emit(
            Resource.Error(
                data = startingData,
                exception = e,
                message = if (e is HttpException) e.message() else e.message,
                errorType = when (e) {
                    is SocketTimeoutException /* For timeout */, is HttpException /*Exception for an unexpected, non-2xx HTTP response.
                    */ -> APIErrorType.Backend
                    else -> APIErrorType.Generic /* Could happen due to invalid mapping business logic or some Retrofit abnormality */
                }
            )
        )
    }.flowOn(Dispatchers.IO)
}