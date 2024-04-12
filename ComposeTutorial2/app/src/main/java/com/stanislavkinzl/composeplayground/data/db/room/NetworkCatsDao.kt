package com.stanislavkinzl.composeplayground.data.db.room

import androidx.room.Dao
import androidx.room.Query
import com.stanislavkinzl.composeplayground.data.db.room.base.BaseDao
import com.stanislavkinzl.composeplayground.data.NetworkCat
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NetworkCatsDao : BaseDao<NetworkCat>(
    tableName = NetworkCat::class.java.simpleName
) {

    // Flow base generic functions for Room Dao's seems not to be working
    // https://stackoverflow.com/questions/68891748/room-queries-in-generic-base-class-especially-flow-queries
    @Query("SELECT * FROM NetworkCat")
    abstract fun observeNetworkCatEntries(): Flow<List<NetworkCat>?>
}