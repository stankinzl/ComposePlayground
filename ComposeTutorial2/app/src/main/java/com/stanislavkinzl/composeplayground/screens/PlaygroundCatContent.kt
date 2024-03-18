package com.stanislavkinzl.composeplayground.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stanislavkinzl.composeplayground.Global
import com.stanislavkinzl.composeplayground.data.PlaygroundCat
import com.stanislavkinzl.composeplayground.ui.SpacerVertical

@Composable
fun PlaygroundCatContent(cat: PlaygroundCat) {
    // Custom bottom sheet height https://stackoverflow.com/questions/69529798/how-to-expand-bottomsheetscaffold-to-a-specific-height-at-with-jetpack-compose
    Column(
        modifier = Modifier
            .heightIn(min = 100.dp, max = 500.dp)
            .safeContentPadding()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = cat.catImageURI,
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp),
            contentScale = ContentScale.FillBounds
        )
        SpacerVertical(height = Global.mediumGap)
        Text("Cat's name: ${cat.catName}")
        SpacerVertical(height = Global.mediumGap)
        Text("Cat's password: ${cat.catPassword}")
    }
}