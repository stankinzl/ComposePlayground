package com.stanislavkinzl.composeplayground.screens.navigation

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.NavController

@Composable
fun ComposeNavigationPassArgumentsScreen(navController: NavController) {
    NavigationSamplePassArgumentsScreen(onNavigateToCat = { cat ->
        val destination = navController.graph.findNode(nativeNavigationDestinationCatSample)
        if (destination != null) {
            navController.navigate(
                destination.id,
                bundleOf(catArgumentName to cat)
            )
        }
    })
}