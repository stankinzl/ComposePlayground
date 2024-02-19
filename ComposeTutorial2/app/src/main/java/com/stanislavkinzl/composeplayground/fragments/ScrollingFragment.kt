package com.stanislavkinzl.composeplayground.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stanislavkinzl.composeplayground.Global
import com.stanislavkinzl.composeplayground.SpacerVertical
import com.stanislavkinzl.composeplayground.base.BaseComposeFragment
import com.stanislavkinzl.composeplayground.log
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.launch

class ScrollingFragment : BaseComposeFragment() {
    override fun onShouldInflateComposeView(composeView: ComposeView) {
        composeView.setContent {
            ComposePlaygroundTheme {
                ScrollingScreen()
            }
        }
    }
}

const val DEFAULT_ANIMATED_SCROLL_DP = 100
const val DEFAULT_ANIMATED_SCROLL_INCREMENT_DP = 100

// TODO: Add scroll to child.

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScrollingScreen() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var smoothScrollValue by remember { mutableIntStateOf(100) }
    var counter by remember { mutableIntStateOf(0) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp) // matters to be last for overscroll effect be outside of padding range
        ) {
            Text("Smooth Scroll value: $smoothScrollValue", fontSize = 12.sp)

            SpacerVertical(space = Global.mediumGap)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Smooth Scroll Button
                Button(onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(smoothScrollValue)
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

            SpacerVertical(space = Global.mediumGap)

            Row {
                Text("Enter scroll to child (up to 200):", fontSize = 12.sp)
            }

            SpacerVertical(space = Global.mediumGap)

            SideEffect {
                log("Button counter is $counter")
            }
            Text(text = "Counter: $counter")
            ElevatedButton(onClick = { counter++ }, modifier = Modifier.padding(top = 16.dp)) {
                Text("Increment")
            }

            SpacerVertical(space = Global.mediumGap)

            (0..200).forEach {
                Text("Line $it")
            }
        }
    }
}

@Preview
@Composable
fun AppScreenPreview() {
    ComposePlaygroundTheme {
        ScrollingScreen()
    }
}