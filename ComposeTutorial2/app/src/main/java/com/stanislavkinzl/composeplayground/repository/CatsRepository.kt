package com.stanislavkinzl.composeplayground.repository

import com.stanislavkinzl.composeplayground.common.Resource
import com.stanislavkinzl.composeplayground.data.NetworkCat
import com.stanislavkinzl.composeplayground.data.db.room.NetworkCatsDao
import com.stanislavkinzl.composeplayground.network.CatsApiService
import com.stanislavkinzl.composeplayground.network.CatsApiService.Companion.DEFAULT_CAT_REQUEST_LIMIT_20
import com.stanislavkinzl.composeplayground.network.helpers.NetworkMonitor
import com.stanislavkinzl.composeplayground.repository.mapper.NetworkCatMapper
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class CatsRepository @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val catsApiService: CatsApiService,
    private val networkCatMapper: NetworkCatMapper,
    private val networkCatsDao: NetworkCatsDao
) {

    suspend fun fetchNextPageOfCats(): Flow<Resource<List<NetworkCat>>> {
        val databaseCats = networkCatsDao.getAll()
        val downloadedPages = (databaseCats?.size ?: 0).floorDiv(DEFAULT_CAT_REQUEST_LIMIT_20)
        val nextPage = downloadedPages + 1
        Timber.e("xxx nextPage is $nextPage")
        return apiFlow(
            networkMonitor = networkMonitor,
            initialData = { networkCatsDao.getAll() },
            apiOperation = { catsApiService.fetchCats(page = nextPage) },
            successMappingOperation = {
                (databaseCats ?: emptyList()).plus(networkCatMapper.map(it))
            },
            withMappedData = { networkCatsDao.insertOrUpdate(it) }
        )
    }
}