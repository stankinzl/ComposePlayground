package com.stanislavkinzl.composetutorial2

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.stanislavkinzl.composetutorial2.ui.theme.ComposeTutorial2Theme

@Composable
fun ButtonCounter() {
    var counter by remember { mutableIntStateOf(0) }
    SideEffect {
        log("Button counter is $counter")
    }
    Text(text = "Counter: $counter")
    ElevatedButton(onClick = { counter++ }, modifier = Modifier.padding(top = 16.dp)) {
        Text("Increment")
    }
}

@PreviewLightDark
@Composable
fun ButtonCounterPreview() {
    ComposeTutorial2Theme {
        ButtonCounter()
    }
}