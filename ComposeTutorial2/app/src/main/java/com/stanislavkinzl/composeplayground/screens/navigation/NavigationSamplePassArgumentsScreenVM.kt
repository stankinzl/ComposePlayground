package com.stanislavkinzl.composeplayground.screens.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanislavkinzl.composeplayground.WhileViewSubscribed
import com.stanislavkinzl.composeplayground.data.PlaygroundCat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NavigationSamplePassArgumentsScreenVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val KEY_CAT_NAME = "KEY_CAT_NAME"
        const val KEY_CAT_IMAGE_URI = "KEY_CAT_IMAGE_URI"
        const val KEY_CAT_PASSWORD_SHOW = "KEY_CAT_PASSWORD_SHOW"
        const val KEY_HAS_AUTO_OPENED_IME = "KEY_HAS_AUTO_OPENED_IME"
    }

    val hasAutoOpenedIme = savedStateHandle.getStateFlow(KEY_HAS_AUTO_OPENED_IME, false)
    val catName = savedStateHandle.getStateFlow(KEY_CAT_NAME, "") // survives process death
    val catImageUri = savedStateHandle.getStateFlow<Uri?>(KEY_CAT_IMAGE_URI, null)
    private val catPassword = MutableStateFlow("")
    val showPassword = savedStateHandle.getStateFlow(KEY_CAT_PASSWORD_SHOW, false)

    val catInfo = combine(catName, catImageUri, catPassword) { catName, catImageUri, catPassword ->
        PlaygroundCat(catName, catPassword, catImageUri)
    }.stateIn(viewModelScope, WhileViewSubscribed, PlaygroundCat(catName.value, catPassword.value, catImageUri.value))

    val isContinueButtonEnabled = catInfo.map {
        it.catImageURI != null && it.catName.isNotEmpty() && it.catPassword.isNotEmpty()
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    fun updateCatName(updatedCatName: String) {
        savedStateHandle[KEY_CAT_NAME] = updatedCatName
    }

    fun updateCatPassword(catPassword: String) {
        this.catPassword.value = catPassword
    }

    fun updateCatImageUri(updatedCatImageUri: Uri?) {
        if (updatedCatImageUri == null) return // No image was selected, don't update
        savedStateHandle[KEY_CAT_IMAGE_URI] = updatedCatImageUri
    }

    fun removeSelectedImageUri() {
        savedStateHandle[KEY_CAT_IMAGE_URI] = null
    }

    fun switchPasswordVisibility() {
        savedStateHandle[KEY_CAT_PASSWORD_SHOW] = !showPassword.value
    }

    fun setHasAutoOpenedIme(hasOpened: Boolean) {
        savedStateHandle[KEY_HAS_AUTO_OPENED_IME] = hasOpened
    }
}