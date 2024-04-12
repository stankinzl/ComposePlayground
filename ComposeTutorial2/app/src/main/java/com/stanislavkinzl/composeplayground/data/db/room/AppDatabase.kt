package com.stanislavkinzl.composeplayground.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stanislavkinzl.composeplayground.data.NetworkCat

@Database(
    entities = [NetworkCat::class],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catsDao(): NetworkCatsDao
}