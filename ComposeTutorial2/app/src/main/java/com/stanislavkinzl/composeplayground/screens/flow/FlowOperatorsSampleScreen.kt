package com.stanislavkinzl.composeplayground.screens.flow

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.stanislavkinzl.composeplayground.R
import com.stanislavkinzl.composeplayground.fillWidthOfParent
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.SpacerVertical

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SetTextI18n")
@Destination
@Composable
fun FlowOperatorsSampleScreen(
    viewModel: FlowOperatorsSampleScreenVM = hiltViewModel<FlowOperatorsSampleScreenVM>()
) {
    val transformOutput = viewModel.transformOutput.collectAsStateWithLifecycle()
    val flatMapConcatOutput = viewModel.flatMapConcatOutput.collectAsStateWithLifecycle()

    @Composable
    fun LazyItemScope.CodeSnippetImage(@DrawableRes imageRes: Int) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "transform sample",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .animateItemPlacement()
        )
    }

    @Composable
    fun LazyItemScope.CodeSnippetHeader(text: String) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.animateItemPlacement()
                .padding(horizontal = 16.dp)
        )
    }

    @Composable
    fun LazyItemScope.CodeOutput(text: String) {
        Text("Output", modifier = Modifier.padding(horizontal = 16.dp))
        SpacerVertical(height = 8.dp)
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .animateItemPlacement(),
            lineHeight = 14.sp
        )
    }

    @Composable
    fun LazyItemScope.CodeSnippetItem(
        header: String,
        @DrawableRes imageRes: Int,
        output: String
    ) {
        SpacerVertical(height = 16.dp)
        CodeSnippetHeader(header)
        SpacerVertical(height = 8.dp)
        CodeSnippetImage(imageRes = imageRes)
        SpacerVertical(height = 8.dp)
        CodeOutput(text = output)
        SpacerVertical(height = 16.dp)
        HorizontalDivider(modifier = Modifier.animateItemPlacement())
    }

    DefaultSurface {
        LazyColumn {
            item("1") {
                CodeSnippetItem(
                    header = "transform",
                    imageRes = R.drawable.flow_transform_sample,
                    output = transformOutput.value
                )
            }
            item("2") {
                CodeSnippetItem(
                    header = "flatMapConcat",
                    imageRes = R.drawable.flow_flat_map_concat_sample,
                    output = flatMapConcatOutput.value
                )
            }
        }
    }
}