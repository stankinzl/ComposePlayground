package com.stanislavkinzl.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.Alignment
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.stanislavkinzl.composeplayground.screens.NavGraphs
import com.stanislavkinzl.composeplayground.screens.RouterScreen
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val rootDefaultAnimations by lazy {
            RootNavGraphDefaultAnimations(
                enterTransition = { slideInHorizontally(initialOffsetX = { it }).plus(fadeIn()) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }).plus(fadeOut()) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }).plus(fadeIn()) },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }).plus(fadeOut()) }
            )
        }
        setContent {
            ComposePlaygroundTheme {
                val navController = rememberNavController()
                val navHostEngine = rememberAnimatedNavHostEngine(
                    navHostContentAlignment = Alignment.TopCenter,
                    rootDefaultAnimations = rootDefaultAnimations,
                    defaultAnimationsForNestedNavGraph = mapOf(
                        NavGraphs.root to NestedNavGraphDefaultAnimations(
                            enterTransition = { slideInHorizontally() },
                            exitTransition = { slideOutHorizontally() }
                        ),
                    ))
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = navController,
                    engine = navHostEngine
                )
//                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}