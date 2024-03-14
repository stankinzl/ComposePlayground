package com.stanislavkinzl.composeplayground.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// source https://stackoverflow.com/questions/77026273/android-create-custom-colors-in-compose-with-material-3

@Immutable
data class CustomColorsPalette(
    val animatedButtonContent: Color = Color.Unspecified,
    val animatedButtonContainer: Color = Color.Unspecified,
    val animatedButtonContainerDisabled: Color = Color.Unspecified,
    val animatedButtonContentDisabled: Color = Color.Unspecified
)

val LightCustomColorsPalette = CustomColorsPalette(
    animatedButtonContainer = Color.White,
    animatedButtonContent = Purple40,
    animatedButtonContainerDisabled = Gainsboro,
    animatedButtonContentDisabled = Nobel
)

// TODO: Set custom colors for Dark AnimatedButton
val DarkCustomColorsPalette = CustomColorsPalette(
    animatedButtonContainer = Color.White,
    animatedButtonContent = Purple40,
    animatedButtonContainerDisabled = Gainsboro,
    animatedButtonContentDisabled = Nobel
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }

@Suppress("UnusedReceiverParameter")
val MaterialTheme.customColorScheme: CustomColorsPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorsPalette.current