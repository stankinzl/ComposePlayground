package com.stanislavkinzl.composeplayground

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun log(string: String) {
    Log.d("Logging", string)
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.newBringIntoViewRequesterModifier(
    bringIntoViewRequester: BringIntoViewRequester,
    scope: CoroutineScope
) = this.then(
    Modifier
        .bringIntoViewRequester(bringIntoViewRequester)
        .onFocusEvent { focusState ->
            if (focusState.isFocused) {
                scope.launch {
                    bringIntoViewRequester.bringIntoView()
                }
            }
        })

/** Tutorial for composed custom modifiers: */
// https://jorgecastillo.dev/composed-modifiers-in-jetpack-compose
/*
  Seems like this bug still exists even in the newest Compose version, even though some people claim it has been fixed
* https://stackoverflow.com/questions/68389802/how-to-clear-textfield-focus-when-closing-the-keyboard-and-prevent-two-back-pres/68420874#68420874
* */
@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}