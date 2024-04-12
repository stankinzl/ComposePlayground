package com.stanislavkinzl.composeplayground.screens.flow

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun FlowOperatorsSampleScreen(
    viewModel: FlowOperatorsSampleScreenVM = hiltViewModel<FlowOperatorsSampleScreenVM>()
) {

}