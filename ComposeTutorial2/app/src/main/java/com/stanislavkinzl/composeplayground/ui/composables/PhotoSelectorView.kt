package com.stanislavkinzl.composeplayground.ui.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PhotoSelectorView(maxSelectionCount: Int = 10, onResult: (List<Uri?>) -> Unit) {
    val buttonText = if (maxSelectionCount > 1) {
        "Select up to $maxSelectionCount photos"
    } else {
        "Select a photo"
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onResult.invoke(listOf(uri)) }
    )

    // I will start this off by saying that I am still learning Android development:
    // We are tricking the multiple photos picker here which is probably not the best way,
    // if you know of a better way to implement this feature drop a comment and let me know
    // how to improve this design
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = if (maxSelectionCount > 1) {
                maxSelectionCount
            } else {
                2
            }
        ),
        onResult = { uris -> onResult.invoke(uris) }
    )

    fun launchPhotoPicker() {
        if (maxSelectionCount > 1) {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = {
            launchPhotoPicker()
        }) {
            Text(buttonText)
        }
    }
}