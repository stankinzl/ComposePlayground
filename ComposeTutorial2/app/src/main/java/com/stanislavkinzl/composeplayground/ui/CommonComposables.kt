package com.stanislavkinzl.composeplayground.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SpacerVertical(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun SpacerHorizontal(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun DefaultSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize()
            .safeContentPadding()
            .then(modifier),
        color = MaterialTheme.colorScheme.background,
        content = content
    )
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