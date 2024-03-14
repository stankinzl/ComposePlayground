package com.stanislavkinzl.composeplayground.screens

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.stanislavkinzl.composeplayground.Global
import com.stanislavkinzl.composeplayground.log
import com.stanislavkinzl.composeplayground.ui.DefaultScrollableColumn
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.launch

const val DEFAULT_ANIMATED_SCROLL_DP = 100
const val DEFAULT_ANIMATED_SCROLL_INCREMENT_DP = 100

// TODO: Add scroll to child.
// Lazy layouts https://www.youtube.com/watch?v=1ANt65eoNhQ&t=896s
/**
 * Flow Row
* */
@Destination
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScrollingScreen() {
    val scope = rememberCoroutineScope()
    var smoothScrollValue by rememberSaveable { mutableIntStateOf(100) }
    var counter by rememberSaveable { mutableIntStateOf(0) }
    val columnState = rememberLazyListState()

    DefaultSurface {
        LazyColumn(
            state = columnState,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(16.dp)
        ) {
            item { Text("Smooth Scroll value: $smoothScrollValue", fontSize = 12.sp) }
            item { SpacerVertical(height = Global.mediumGap) }
            // buttons
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
            item { SpacerVertical(height = Global.mediumGap) }
            item {
                Row {
                    Text("Enter scroll to child (up to 200):", fontSize = 12.sp)
                    // TODO: add functionality
                }
            }
            item { SpacerVertical(height = Global.mediumGap) }
            item { Text(text = "Counter: $counter") }
            item {
                ElevatedButton(onClick = { counter++ }, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Increment")
                }
            }
            item { SpacerVertical(height = Global.mediumGap) }
            (0..200).forEach {
                item {
                    Text("Line $it")
                }
            }
        }

        SideEffect {
            log("Button counter is $counter")
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