package com.stanislavkinzl.composeplayground.network.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatResponse(
    val id: String,
    val breeds: List<CatBreedResponse>? = null,
    val url: String? = null
)

@JsonClass(generateAdapter = true)
data class CatBreedResponse(
    val name: String? = null,
    val origin: String? = null
)