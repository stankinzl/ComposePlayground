package com.stanislavkinzl.composeplayground.screens.xmlnavigationsample

import androidx.compose.ui.platform.ComposeView
import com.stanislavkinzl.composeplayground.screens.navigation.NavigationSamplePassArgumentsScreen
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme

class PassArgumentsFragment : BaseComposeFragment() {
    override fun onShouldInflateComposeView(composeView: ComposeView) {
        composeView.setContent {
            ComposePlaygroundTheme {
                NavigationSamplePassArgumentsScreen()
            }
        }
    }
}