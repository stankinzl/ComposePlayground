package com.stanislavkinzl.composeplayground.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stanislavkinzl.composeplayground.ui.DefaultScrollableColumn
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationSamplePassArgumentsScreen() {
    val scrollState = rememberScrollState()
    DefaultSurface {
        // Text input 1
        DefaultScrollableColumn(scrollState = scrollState) {
            Text(text = "Cat name")
            val catNameInputState = TextFieldState()
            BasicTextField2(state = catNameInputState)
        }
    }
}

@Preview
@Composable
fun NavigationSamplePassArgumentsScreenPreview() {
    ComposePlaygroundTheme {
        NavigationSamplePassArgumentsScreen()
    }
}