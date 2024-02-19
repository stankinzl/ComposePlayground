package com.stanislavkinzl.composeplayground.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpacerVertical(space: Int) {
    Spacer(modifier = Modifier.height(space.dp))
}

@Composable
fun SpacerHorizontal(space: Int) {
    Spacer(modifier = Modifier.width(space.dp))
}

@Composable
fun DefaultSurface(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background, content = content)
}

@Composable
fun DefaultScrollableColumn(scrollState: ScrollState, content: @Composable ColumnScope.() -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp), // matters to be last for overscroll effect be outside of padding range
        content = content
    )
}