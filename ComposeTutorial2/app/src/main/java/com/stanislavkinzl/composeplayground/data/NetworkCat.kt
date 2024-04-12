package com.stanislavkinzl.composeplayground.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NetworkCat(
    val breedName: String,
    val origin: String,
    val url: String,
    @PrimaryKey val databaseKey: String
)