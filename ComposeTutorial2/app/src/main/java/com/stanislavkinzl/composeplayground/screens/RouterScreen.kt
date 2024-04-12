package com.stanislavkinzl.composeplayground.screens

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.stanislavkinzl.composeplayground.getActivity
import com.stanislavkinzl.composeplayground.screens.destinations.DirectionDestination
import com.stanislavkinzl.composeplayground.screens.destinations.FlowOperatorsSampleScreenDestination
import com.stanislavkinzl.composeplayground.screens.destinations.KoinSampleScreenDestination
import com.stanislavkinzl.composeplayground.screens.destinations.KtorRealmCatSampleScreenDestination
import com.stanislavkinzl.composeplayground.screens.destinations.NativeComposeNavigationSampleDestination
import com.stanislavkinzl.composeplayground.screens.destinations.RetrofitRoomCatSampleScreenDestination
import com.stanislavkinzl.composeplayground.screens.destinations.SQLiteSampleScreenDestination
import com.stanislavkinzl.composeplayground.screens.destinations.ScrollingScreenDestination
import com.stanislavkinzl.composeplayground.screens.xmlnavigationsample.XmlNavigationComponentActivity
import com.stanislavkinzl.composeplayground.toast
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import com.stanislavkinzl.composeplayground.ui.theme.DarkGreen
import com.stanislavkinzl.composeplayground.ui.theme.DarkPurple
import com.stanislavkinzl.composeplayground.ui.theme.LightBlue
import com.stanislavkinzl.composeplayground.ui.theme.Orange
import com.stanislavkinzl.composeplayground.ui.theme.PrettyGreen

@Destination(start = true)
@Composable
fun RouterScreen(navigator: DestinationsNavigator? = null) {
    val context = LocalContext.current

    @Composable
    fun RouterButton(
        destination: DirectionDestination,
        text: String,
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary
    ) {
        Button(colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ), onClick = {
            navigator?.navigate(destination, onlyIfResumed = true)
        }) {
            Text(text)
        }
    }

    DefaultSurface {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 16.dp)
        ) {
            item {
                RouterButton(ScrollingScreenDestination, "Scrolling screen")
            }
            item {
                Button(colors = ButtonDefaults.buttonColors(containerColor = DarkGreen), onClick = {
                    context.getActivity()?.startActivity(Intent(context, XmlNavigationComponentActivity::class.java))
                        ?: run { context.toast("Error: cannot get activity from context") /* Shouldn't happen */ }
                }) {
                    Text("XML Navigation Component w Compose sample")
                }
            }
            item {
                RouterButton(
                    NativeComposeNavigationSampleDestination,
                    "Native Compose navigation Sample",
                    contentColor = Color.Black,
                    containerColor = PrettyGreen
                )
            }
            item {
                RouterButton(
                    KoinSampleScreenDestination,
                    "Koin Sample Screen",
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                )
            }
            item {
                RouterButton(
                    RetrofitRoomCatSampleScreenDestination,
                    "Retrofit & Room sample (WiP)",
                    containerColor = Orange,
                    contentColor = Color.Black
                )
            }
            item {
                RouterButton(
                    KtorRealmCatSampleScreenDestination,
                    "Ktor & Realm sample (WiP)",
                    containerColor = LightBlue,
                    contentColor = Color.Blue
                )
            }
            item {
                RouterButton(
                    SQLiteSampleScreenDestination,
                    "SQLite sample",
                    containerColor = Color.Blue
                )
            }
            item {
                RouterButton(
                    FlowOperatorsSampleScreenDestination,
                    "Flow operators sample (WiP)",
                    containerColor = DarkPurple
                )
            }
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