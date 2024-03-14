package com.stanislavkinzl.composeplayground.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaygroundCat(
    val catName: String,
    val catPassword: String,
    val catImageURI: Uri?
) : Parcelable