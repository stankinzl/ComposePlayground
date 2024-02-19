package com.stanislavkinzl.composetutorial2.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.stanislavkinzl.composetutorial2.base.BaseComposeFragment
import com.stanislavkinzl.composetutorial2.ui.theme.ComposeTutorial2Theme

class RouterFragment : BaseComposeFragment() {
    override fun onShouldInflateComposeView(composeView: ComposeView) {
        composeView.setContent {
            ComposeTutorial2Theme {
                RouterScreen(findNavController())
            }
        }
    }
}

@Composable
fun RouterScreen(navController: NavController? = null) {
    val scrollState = rememberScrollState()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp) // matters to be last for overscroll effect be outside of padding range
        ) {
            Button(onClick = {
                navController?.navigate(RouterFragmentDirections.actionRouterFragmentToScrollingFragment())
            }) {
                Text("Scrolling screen")
            }
        }
    }
}

@Preview
@Composable
fun RouterScreenPreview() {
    ComposeTutorial2Theme {
        RouterScreen()
    }
}