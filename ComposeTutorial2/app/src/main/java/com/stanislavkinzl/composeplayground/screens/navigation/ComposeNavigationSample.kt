package com.stanislavkinzl.composeplayground.screens.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.stanislavkinzl.composeplayground.data.PlaygroundCat
import com.stanislavkinzl.composeplayground.domain.CommonNavigationHelper

const val nativeNavigationDestinationCatSample = "catSampleScreen"

const val catArgumentName = "cat"

@Composable
@Destination
fun NativeComposeNavigationSample(
    navigationHelper: CommonNavigationHelper // Just seeing how easy injecting dependencies into Composables is
) {
    // https://developer.android.com/guide/navigation/design#compose
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "pass_arguments",
        enterTransition = { slideInHorizontally(initialOffsetX = { it }).plus(fadeIn()) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }).plus(fadeOut()) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }).plus(fadeIn()) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }).plus(fadeOut()) }
    ) {
        composable("pass_arguments") {
            ComposeNavigationPassArgumentsScreen(navController)
        }
        composable(nativeNavigationDestinationCatSample) {
            // https://developer.android.com/jetpack/compose/navigation#:~:text=Navigation%20Compose%20also%20supports%20optional,the%20default%20value%20to%20null%20)
            // It is strongly advised not to pass around complex data objects when navigating,
            // but instead pass the minimum necessary information, such as a unique identifier
            // or other form of ID, as arguments when performing navigation actions:
            val cat = it.arguments?.getParcelable(catArgumentName, PlaygroundCat::class.java)
            NavigationSampleCatScreen(cat!!) // i want it to throw knp if it doesn't work
        }
        // Add more destinations similarly.
    }
}