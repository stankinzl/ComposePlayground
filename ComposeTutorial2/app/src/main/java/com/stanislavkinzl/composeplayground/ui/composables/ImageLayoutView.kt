package com.stanislavkinzl.composeplayground.ui.composables

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun ImageLayoutView(
    selectedImages: List<Uri?>,
    @SuppressLint("ModifierParameter") imageModifier: Modifier = Modifier
) {
    if (selectedImages.isEmpty() || selectedImages.firstOrNull() == null) return
    LazyRow {
        items(selectedImages) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .then(imageModifier),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}
