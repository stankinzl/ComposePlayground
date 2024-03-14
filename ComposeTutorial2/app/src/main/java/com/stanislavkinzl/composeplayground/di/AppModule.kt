package com.stanislavkinzl.composeplayground.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppModule {

}

fun <VM: ViewModel> viewModelFactory(initializer: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return initializer() as T
        }
    }
}