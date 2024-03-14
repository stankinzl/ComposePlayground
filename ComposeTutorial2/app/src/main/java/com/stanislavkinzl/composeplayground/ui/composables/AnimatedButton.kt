package com.stanislavkinzl.composeplayground.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.stanislavkinzl.composeplayground.ui.theme.customColorScheme

@Composable
fun AnimatedButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animationSpec = tween<Color>(durationMillis = 250, easing = LinearEasing)
    val animatedContainerColor = animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.customColorScheme.animatedButtonContainer
        } else {
            MaterialTheme.customColorScheme.animatedButtonContainerDisabled
        },
        animationSpec = animationSpec,
        label = ""
    )
    val animatedContentColor = animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.customColorScheme.animatedButtonContent
        } else {
            MaterialTheme.customColorScheme.animatedButtonContentDisabled
        },
        animationSpec = animationSpec,
        label = ""
    )
    ElevatedButton(
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedContainerColor.value,
            disabledContainerColor = animatedContainerColor.value,
            contentColor = animatedContentColor.value,
            disabledContentColor = animatedContentColor.value
        ),
        enabled = enabled,
        modifier = modifier,
        onClick = {
            onClick.invoke()
        }) {
        Text(text)
    }
}