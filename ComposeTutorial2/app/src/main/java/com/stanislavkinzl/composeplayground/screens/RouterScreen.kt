package com.stanislavkinzl.composeplayground.screens

import android.content.Intent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.stanislavkinzl.composeplayground.Global
import com.stanislavkinzl.composeplayground.getActivity
import com.stanislavkinzl.composeplayground.screens.xmlnavigationsample.XmlNavigationComponentActivity
import com.stanislavkinzl.composeplayground.toast
import com.stanislavkinzl.composeplayground.ui.DefaultScrollableColumn
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import com.stanislavkinzl.composeplayground.ui.theme.DarkGreen

@Composable
fun RouterScreen() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    DefaultSurface {
        DefaultScrollableColumn(scrollState = scrollState) {
            Button({ /* todo: navigate to scrolling screen */ }) { Text("Scrolling screen") }
            SpacerVertical(space = Global.smallGap)
            Button(colors = ButtonDefaults.buttonColors(containerColor = DarkGreen), onClick = {
                context.getActivity()?.startActivity(Intent(context, XmlNavigationComponentActivity::class.java))
                    ?: run { context.toast("Error: cannot get activity from context") /* Shouldn't happen */ }
            }) { Text("XML Navigation Component w Compose sample") }
        }
    }
}

@Preview
@Composable
fun RouterScreenPreview() {
    ComposePlaygroundTheme {
        RouterScreen()
    }
}