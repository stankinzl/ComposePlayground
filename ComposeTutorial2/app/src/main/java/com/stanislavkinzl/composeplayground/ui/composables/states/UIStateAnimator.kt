package com.stanislavkinzl.composeplayground.ui.composables.states

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.stanislavkinzl.composeplayground.R.drawable
import com.stanislavkinzl.composeplayground.common.UiState
import com.stanislavkinzl.composeplayground.common.UiState.Content
import com.stanislavkinzl.composeplayground.common.UiState.Empty
import com.stanislavkinzl.composeplayground.common.UiState.FullscreenLoading
import com.stanislavkinzl.composeplayground.common.UiState.GenericError
import com.stanislavkinzl.composeplayground.common.UiState.NotConnected
import com.stanislavkinzl.composeplayground.common.UiState.Other
import com.stanislavkinzl.composeplayground.common.UiState.ServerError
import com.stanislavkinzl.composeplayground.ui.DefaultSurface

@Composable
fun <R> UIStateAnimator(
    modifier: Modifier = Modifier,
    uiState: UiState<R>,
    onRetry: () -> Unit,
    fullscreenLoading: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    },
    notConnectedErrorScreen: @Composable (NotConnected<R>) -> Unit = {
        val context = LocalContext.current
        CommonErrorScreen(
            onButtonClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContextCompat.startActivity(context, Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY), null)
                } else {
                    ContextCompat.startActivity(context, Intent(Settings.ACTION_WIFI_SETTINGS), null)
                }
            },
            iconRes = drawable.ic_wifi_off,
            buttonText = "Open settings",
            message = "You need to connect to the internet for this to work"
        )
    },
    serverErrorScreen: @Composable (ServerError<R>) -> Unit = {
        CommonErrorScreen(
            onButtonClick = { onRetry.invoke() },
            iconRes = drawable.ic_sad,
            message = "The network call didn't work and it seems you are connected. There's a problem on app's/server side",
            buttonText = "Try again"
        )
    },
    genericErrorScreen: @Composable (GenericError<R>) -> Unit = {
        CommonErrorScreen(
            onButtonClick = { onRetry.invoke() },
            iconRes = drawable.ic_error,
            message = "Something went wrong in the app.",
            buttonText = "Try again"
        )
    },
    otherScreen: @Composable (Other<R>) -> Unit = {
        Text(text = "Other pojo. Provide custom other screen")
    },
    // First boolean is for loading mode, second for error mode.
    // It's up to me to decide what to show for either cases
    contentScreen: @Composable (Content<R>) -> Unit = {
        Text(text = "Pojo. Provide custom content screen")
    },
    emptyScreen: @Composable (Empty<R>) -> Unit = {
        Text(text = "Pojo. Provide custom empty screen")
    }
) {
    DefaultSurface(modifier = modifier) {
        when (uiState) {
            is Content -> contentScreen(uiState)
            is Empty -> emptyScreen(uiState)
            is GenericError -> genericErrorScreen(uiState)
            is FullscreenLoading -> fullscreenLoading()
            is NotConnected -> notConnectedErrorScreen(uiState)
            is Other -> otherScreen(uiState)
            is ServerError -> serverErrorScreen(uiState)
        }
    }
}