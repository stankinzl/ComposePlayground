package com.stanislavkinzl.composeplayground.screens.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stanislavkinzl.composeplayground.Global.mediumGap
import com.stanislavkinzl.composeplayground.data.PlaygroundCat
import com.stanislavkinzl.composeplayground.screens.PlaygroundCatContent
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.composables.AnimatedButton
import com.stanislavkinzl.composeplayground.ui.composables.ImePushBox
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSampleCatScreen(cat: PlaygroundCat) {
    val isSheetOpen = rememberSaveable { mutableStateOf(false) }

    PlaygroundCatContent(cat = cat)
    ImePushBox {
        HorizontalDivider()
        AnimatedButton(
            text = "Bottom sheet", enabled = true, onClick = {
                isSheetOpen.value = true
            }, modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )
    }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // see https://medium.com/@avengers14.blogger/material-3-bottom-sheet-draghandle-jetpack-compose-2ec79bae492c
    // where it is explained how to define a custom sheet shape and handle
    if (isSheetOpen.value) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { isSheetOpen.value = false },
            shape = RoundedCornerShape(
                topStart = 2.dp,
                topEnd = 2.dp
            ),
            dragHandle = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Above handle")
                    SpacerVertical(height = mediumGap)
                    Spacer(
                        modifier = Modifier
                            .width(30.dp)
                            .height(5.dp)
                            .background(Color.Red)
                    )
                    SpacerVertical(height = mediumGap)
                    Text("Below handle")
                    HorizontalDivider()
                }
            }
        ) {
            PlaygroundCatContent(cat = cat)
        }
    }
}

@Preview
@Composable
fun NavigationSampleCatScreenPreview() {
    ComposePlaygroundTheme {
        NavigationSampleCatScreen(
            cat = PlaygroundCat(
                catName = "Cat name",
                catPassword = "Cat Password",
                catImageURI = null
            )
        )
    }
}