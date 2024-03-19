package com.stanislavkinzl.composeplayground.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.stanislavkinzl.composeplayground.domain.SampleDependency
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

/**
 * Screen that just uses some of Koin
 * */
@Destination
@Composable
fun KoinSampleScreen(
    viewModel: KoinSampleScreenVM = koinViewModel(),
    sampleDependency: SampleDependency = koinInject()
) {
    DefaultSurface {
        Column {
            Text(text = "Text from koinInject:\n${sampleDependency.dependencyThing()}")
            HorizontalDivider(modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp))
            Text(text = "Text from koinViewModel:\n${viewModel.getText()}")
        }
    }
}

class KoinSampleScreenVM(
    private val koinSampleDependency: SampleDependency
) : ViewModel() {

    fun getText() = koinSampleDependency.dependencyThing()
}