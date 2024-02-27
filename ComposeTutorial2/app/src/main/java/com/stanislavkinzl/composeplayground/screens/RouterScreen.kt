package com.stanislavkinzl.composeplayground.screens

import android.content.Intent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.stanislavkinzl.composeplayground.Global
import com.stanislavkinzl.composeplayground.getActivity
import com.stanislavkinzl.composeplayground.screens.destinations.ScrollingScreenDestination
import com.stanislavkinzl.composeplayground.screens.xmlnavigationsample.XmlNavigationComponentActivity
import com.stanislavkinzl.composeplayground.toast
import com.stanislavkinzl.composeplayground.ui.DefaultScrollableColumn
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import com.stanislavkinzl.composeplayground.ui.theme.DarkGreen

@Destination(start = true)
@Composable
fun RouterScreen(navigator: DestinationsNavigator? = null) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    DefaultSurface {
        DefaultScrollableColumn(scrollState = scrollState) {
            // Screen where I test scrolling
            Button({
                navigator?.navigate(ScrollingScreenDestination)
            }) { Text("Scrolling screen") }

            // XML navigation sample
            SpacerVertical(height = Global.smallGap)
            Button(colors = ButtonDefaults.buttonColors(containerColor = DarkGreen), onClick = {
                context.getActivity()?.startActivity(Intent(context, XmlNavigationComponentActivity::class.java))
                    ?: run { context.toast("Error: cannot get activity from context") /* Shouldn't happen */ }
            }) { Text("XML Navigation Component w Compose sample") }

            // Native Compose navigation sample
            // TODO

            // ConstraintLayout in compose playground
            // TODO

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