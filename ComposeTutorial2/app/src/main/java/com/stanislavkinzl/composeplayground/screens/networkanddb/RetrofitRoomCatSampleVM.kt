package com.stanislavkinzl.composeplayground.screens.networkanddb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanislavkinzl.composeplayground.WhileViewSubscribed
import com.stanislavkinzl.composeplayground.common.RetryChannel
import com.stanislavkinzl.composeplayground.common.UiState
import com.stanislavkinzl.composeplayground.common.toUiState
import com.stanislavkinzl.composeplayground.data.NetworkCat
import com.stanislavkinzl.composeplayground.data.db.room.NetworkCatsDao
import com.stanislavkinzl.composeplayground.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RetrofitRoomCatSampleVM @Inject constructor(
    catsRepository: CatsRepository,
    private val catsDao: NetworkCatsDao
) : ViewModel() {

    private val retryChannel = RetryChannel()

    val uiState: StateFlow<UiState<List<NetworkCat>>> = retryChannel.flatMapLatest {
        catsRepository.fetchNextPageOfCats()
    }.map {
        it.toUiState().also { uiState ->
            Timber.e("xxx: ${uiState.javaClass.name}")
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, UiState.FullscreenLoading())

    fun retry() {
        viewModelScope.launch { retryChannel.retry() } // This will always retry only on the first page, because otherwise we are showing Content
    }

    fun flushCats() {
        viewModelScope.launch {
            catsDao.nukeTable()
            retry()
        }
    }
}