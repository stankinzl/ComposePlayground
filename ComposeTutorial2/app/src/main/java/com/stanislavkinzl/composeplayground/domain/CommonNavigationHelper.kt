package com.stanislavkinzl.composeplayground.domain

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.ramcosta.composedestinations.animations.defaults.DestinationEnterTransition
import com.ramcosta.composedestinations.animations.defaults.DestinationExitTransition
import javax.inject.Inject

class CommonNavigationHelper @Inject constructor() {

    fun commonEnterTransition() = DestinationEnterTransition {
        slideInHorizontally(initialOffsetX = { it }).plus(fadeIn())
    }

    fun commonExitTransition() = DestinationExitTransition {
        slideOutHorizontally(targetOffsetX = { -it }).plus(fadeOut())
    }

    fun commonPopEnterTransition() = DestinationEnterTransition {
        slideInHorizontally(initialOffsetX = { -it }).plus(fadeIn())
    }

    fun commonPopExitTransition() = DestinationExitTransition {
        slideOutHorizontally(targetOffsetX = { it }).plus(fadeOut())
    }
}