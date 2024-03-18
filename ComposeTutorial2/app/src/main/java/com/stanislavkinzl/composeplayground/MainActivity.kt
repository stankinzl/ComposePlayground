package com.stanislavkinzl.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.ramcosta.composedestinations.navigation.dependency
import com.stanislavkinzl.composeplayground.domain.CommonNavigationHelper
import com.stanislavkinzl.composeplayground.screens.NavGraphs
import com.stanislavkinzl.composeplayground.screens.destinations.NativeComposeNavigationSampleDestination
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** Setup for Compose Destinations navigation **/
// see https://composedestinations.rafaelcosta.xyz/
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHelper: CommonNavigationHelper

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables the use of imePadding() modifier
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // region Animations setup for Destinations navigation
        val rootDefaultAnimations by lazy {
            RootNavGraphDefaultAnimations(
                enterTransition = navigationHelper.commonEnterTransition(),
                exitTransition = navigationHelper.commonExitTransition(),
                popEnterTransition = navigationHelper.commonPopEnterTransition(),
                popExitTransition = navigationHelper.commonPopExitTransition()
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
                    engine = navHostEngine,
                    dependenciesContainerBuilder = {
                        // This is the main way of how to provide non-view model dependencies into Destinations.
                        // Due to the potential complexity of this, I would think it'd be better to separate this
                        // dependency injecting logic into a separate class.
                        // From https://composedestinations.rafaelcosta.xyz/common-use-cases/providing-viewmodels/
                        dependency(NativeComposeNavigationSampleDestination) { navigationHelper }
                        // Alternatively Koin seems to work easier with Compose
                        // https://medium.com/@abel.suviri.payan/dependency-injection-in-jetpack-compose-b03d9e51ecae#:~:text=To%20inject%20the%20dependency%20into,to%20the%20dependency%20we%20need.
                    }
                )
            }
        }
        // endregion
    }
}