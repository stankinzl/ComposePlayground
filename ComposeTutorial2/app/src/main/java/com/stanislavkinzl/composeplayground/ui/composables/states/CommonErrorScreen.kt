package com.stanislavkinzl.composeplayground.ui.composables.states

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stanislavkinzl.composeplayground.R
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun CommonErrorScreen(
    onButtonClick: () -> Unit,
    @DrawableRes iconRes: Int,
    message: String,
    buttonText: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = message,
            modifier = Modifier.size(64.dp)
        )
        SpacerVertical(height = 32.dp)
        Text(
            message,
            textAlign = TextAlign.Center
        )
        SpacerVertical(height = 32.dp)
        ElevatedButton(onClick = {
            onButtonClick.invoke()
        }) {
            Text(buttonText)
        }
    }
}

@Preview
@Composable
fun CommonErrorScreenPreview() {
    ComposePlaygroundTheme {
        CommonErrorScreen(
            onButtonClick = {},
            iconRes = R.drawable.ic_error,
            message = "Oops. Something went wrong",
            buttonText = "Click me"
        )
    }
}
