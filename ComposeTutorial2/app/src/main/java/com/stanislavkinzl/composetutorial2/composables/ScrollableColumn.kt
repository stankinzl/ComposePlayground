package com.stanislavkinzl.composetutorial2.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// TODO: Using this causes render issues. Find out why
@Composable
fun ScrollableColumn(
    scrollState: ScrollState,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp) // matters to be last for overscroll effect be outside of padding range
    ) {
        content.invoke(this)
    }
}