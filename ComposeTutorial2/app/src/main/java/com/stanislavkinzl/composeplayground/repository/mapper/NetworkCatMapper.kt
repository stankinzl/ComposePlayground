package com.stanislavkinzl.composeplayground.repository.mapper

import com.stanislavkinzl.composeplayground.data.NetworkCat
import com.stanislavkinzl.composeplayground.network.response.CatResponse
import timber.log.Timber
import javax.inject.Inject

class NetworkCatMapper @Inject constructor() {

    fun map(from: List<CatResponse?>?): List<NetworkCat> = from?.mapNotNull {
        try {
            requireNotNull(it)
            with(requireNotNull(it.breeds?.firstOrNull())) {
                NetworkCat(
                    breedName = name.orEmpty(),
                    origin = origin.orEmpty(),
                    databaseKey = it.id,
                    url = it.url!! // mandatory
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }.orEmpty()
}