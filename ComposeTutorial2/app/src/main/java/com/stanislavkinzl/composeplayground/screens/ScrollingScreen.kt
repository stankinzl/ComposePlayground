package com.stanislavkinzl.composeplayground.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.stanislavkinzl.composeplayground.fillWidthOfParent
import com.stanislavkinzl.composeplayground.log
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import com.stanislavkinzl.composeplayground.ui.theme.DarkGreen
import kotlinx.coroutines.launch

const val DEFAULT_ANIMATED_SCROLL_DP = 100
const val DEFAULT_ANIMATED_SCROLL_INCREMENT_DP = 100

// TODO: Add scroll to child.
// Lazy layouts https://www.youtube.com/watch?v=1ANt65eoNhQ&t=896s
/**
 * Flow Row
 * */
@Destination
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ScrollingScreen() {
    val scope = rememberCoroutineScope()
    var smoothScrollValue by rememberSaveable { mutableIntStateOf(100) }
    val counter by rememberSaveable { mutableIntStateOf(0) }
    val columnState = rememberLazyListState()

    val originalStatusBarColor = MaterialTheme.colorScheme.primary
    val secondStickyHeaderColor = DarkGreen
    val useDarkIcons = isSystemInDarkTheme()
    @Suppress("DEPRECATION")
    val systemUiController = rememberSystemUiController()

    val statusBarColor = remember {
        derivedStateOf {
            val greenStickyHeader= columnState.layoutInfo.visibleItemsInfo.firstOrNull {
                it.key == "second_sticky_header"
            }
            if (greenStickyHeader== null || columnState.firstVisibleItemIndex < greenStickyHeader.index) {
                originalStatusBarColor
            } else {
                secondStickyHeaderColor
            }
        }
    }

    systemUiController.setStatusBarColor(statusBarColor.value, darkIcons = useDarkIcons)

    @Composable
    fun StickyHeader(text: String, backgroundColor: Color) {
        Column(
            modifier = Modifier
                .fillWidthOfParent(16.dp)
                .background(backgroundColor),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    DefaultSurface {
        LazyColumn(
            state = columnState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
        ) {
            item { Text("Smooth Scroll value: $smoothScrollValue", fontSize = 12.sp) }
            // buttons
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {

                    // Smooth Scroll Button
                    Button(onClick = {
                        scope.launch {
                            columnState.animateScrollBy(smoothScrollValue.toFloat())
                        }
                    }) {
                        Text("Smooth Scroll")
                    }

                    // Increment Button
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray // static color
                        ),
                        contentPadding = PaddingValues(8.dp), // custom content padding
                        onClick = {
                            smoothScrollValue += DEFAULT_ANIMATED_SCROLL_INCREMENT_DP
                        }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                        Text("Increment by $DEFAULT_ANIMATED_SCROLL_INCREMENT_DP")
                    }

                    // Reset Button
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        onClick = {
                            smoothScrollValue = DEFAULT_ANIMATED_SCROLL_DP
                        }) {
                        Text("Reset")
                    }
                }
            }
            /*            item {
                            Row {
                                Text("Enter scroll to child (up to 200):", fontSize = 12.sp)
                                // TODO: add functionality
                            }
                        }
                        item { Text(text = "Counter: $counter") }
                        item {
                            ElevatedButton(onClick = { counter++ }) {
                                Text("Increment")
                            }
                        }*/
            stickyHeader {
                StickyHeader("STICKY HEADER", MaterialTheme.colorScheme.primary)
            }
            (0..10).forEach {
                item {
                    Text("Line $it")
                }
            }
            stickyHeader(key = "second_sticky_header") {
                StickyHeader("STICKY HEADER 2", DarkGreen)
            }
            (11..70).forEach {
                item {
                    Text("Line $it")
                }
            }
        }

        SideEffect {
            log("Button counter is $counter")
        }

        DisposableEffect(Unit) {
            onDispose {
                systemUiController.setStatusBarColor(
                    color = originalStatusBarColor,
                    darkIcons = useDarkIcons
                )
            }
        }
    }
}

@Preview
@Composable
fun ScrollingScreenPreview() {
    ComposePlaygroundTheme {
        ScrollingScreen()
    }
}