package com.stanislavkinzl.composeplayground.screens.xmlnavigationsample

import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.navArgs
import com.stanislavkinzl.composeplayground.screens.navigation.NavigationSampleCatScreen
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme

class CatFragment : BaseComposeFragment() {

    private val args by navArgs<CatFragmentArgs>()

    override fun onShouldInflateComposeView(composeView: ComposeView) {
        composeView.setContent {
            ComposePlaygroundTheme {
                NavigationSampleCatScreen(args.cat)
            }
        }
    }
}