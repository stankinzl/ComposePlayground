package com.stanislavkinzl.composetutorial2

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun log(string: String) {
    Log.d("Logging", string)
}

fun coroutine(dispatcher: CoroutineContext = Dispatchers.Default, coroutineBlock: suspend () -> Unit) {
    CoroutineScope(dispatcher).launch {
        coroutineBlock.invoke()
    }
}