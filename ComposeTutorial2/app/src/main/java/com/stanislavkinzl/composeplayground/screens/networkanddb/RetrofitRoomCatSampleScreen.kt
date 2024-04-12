package com.stanislavkinzl.composeplayground.screens.networkanddb

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.stanislavkinzl.composeplayground.common.UiState
import com.stanislavkinzl.composeplayground.data.NetworkCat
import com.stanislavkinzl.composeplayground.fillWidthOfParent
import com.stanislavkinzl.composeplayground.ui.composables.states.UIStateAnimator

@Destination
@Composable
fun RetrofitRoomCatSampleScreen(
    viewModel: RetrofitRoomCatSampleVM = hiltViewModel<RetrofitRoomCatSampleVM>()
) {
    val resource = viewModel.uiState.collectAsStateWithLifecycle()
    UIStateAnimator(
        uiState = resource.value,
        onRetry = { viewModel.retry() },
        contentScreen = { contentState ->
            CatSampleContentScreen(contentState, flushCatsCallback = {
                viewModel.flushCats()
            })
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatSampleContentScreen(
    contentState: UiState.Content<List<NetworkCat>>,
    flushCatsCallback: () -> Unit
) {
    val columnState = rememberLazyListState()
    val loaderIsVisible = remember {
        derivedStateOf {
            columnState.layoutInfo.visibleItemsInfo.lastOrNull()?.key == "loader"
        }
    }


    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        state = columnState
    ) {
        contentState.contentErrorMode?.let {
            item("error_mode_header") {
                Box(
                    modifier = Modifier
                        .fillWidthOfParent(parentPadding = 16.dp)
                        .background(Color.Red)
                ) {
                    Text(it.message, color = Color.White, modifier = Modifier.padding(4.dp))
                }
            }
        }

        item("flush_cats_button") {
            Button(onClick = {
                flushCatsCallback.invoke()
            }) {
                Text("Flush cats & refresh")
            }
        }

        itemsIndexed(
            items = contentState.data,
            key = { _, networkCat -> networkCat.databaseKey }
        ) { index, networkCat ->
            Box(modifier = Modifier.animateItemPlacement()) {
                if (index != 0) HorizontalDivider()
                Text(networkCat.breedName)
            }
        }

        item("loader") {
            CircularProgressIndicator()
        }
    }
}