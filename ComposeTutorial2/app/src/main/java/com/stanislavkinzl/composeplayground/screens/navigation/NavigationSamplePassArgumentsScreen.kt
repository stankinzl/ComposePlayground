package com.stanislavkinzl.composeplayground.screens.navigation

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.input.TextObfuscationMode
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.stanislavkinzl.composeplayground.Global.mediumGap
import com.stanislavkinzl.composeplayground.Global.smallGap
import com.stanislavkinzl.composeplayground.clearFocusOnKeyboardDismiss
import com.stanislavkinzl.composeplayground.newBringIntoViewRequesterModifier
import com.stanislavkinzl.composeplayground.ui.DefaultScrollableColumn
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.bounceClickEffect
import com.stanislavkinzl.composeplayground.ui.composables.AnimatedButton
import com.stanislavkinzl.composeplayground.ui.composables.ImageLayoutView
import com.stanislavkinzl.composeplayground.ui.composables.ImePushBox
import com.stanislavkinzl.composeplayground.ui.composables.PhotoSelectorView
import com.stanislavkinzl.composeplayground.ui.composables.SimpleAlertDialog
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// TODO: Deal with https://stackoverflow.com/questions/5423571/prevent-activity-stack-from-being-restored
/**
 * Playground for TextFields, auto-open IME, push on IME open, Photo Picker, etc. */
/** Alert dialog */
// https://developer.android.com/jetpack/compose/components/dialog
/** TextFieldInput2 */
// https://proandroiddev.com/basictextfield2-a-textfield-of-dreams-1-2-0103fd7cc0ec
/** Photo picker */
// https://medium.com/@jpmtech/jetpack-compose-display-a-photo-picker-6bcb9b357a3a
// https://coil-kt.github.io/coil/compose/
/** Keyboard tutorial: */
// https://blog.canopas.com/keyboard-handling-in-jetpack-compose-all-you-need-to-know-3e6fddd30d9a
// DialogFragment issues: https://patrykkosieradzki.com/how-to-fix-keyboard-issues-introduced-in-the-latest-jetpack-compose-1-4-0/
/** remember vs rememberSavable tutorial: */
// https://medium.com/@waghmaremayur855/understanding-the-difference-between-remember-and-remembersaveable-in-jetpack-compose-29d7231053e5#:~:text=remember%20is%20a%20fundamental%20part,value%20within%20a%20composable%20function.
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationSamplePassArgumentsScreen(
    viewModel: NavigationSamplePassArgumentsScreenVM = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val catNameFieldFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val openRemoveImageAlertDialog = rememberSaveable { mutableStateOf(false) }

    val catNameInput: String by viewModel.catName.collectAsStateWithLifecycle()
    val passwordState = rememberTextFieldState()
    LaunchedEffect(Unit) {
        passwordState.textAsFlow().onEach { // Ugly solution, instead of VM saving the passwordState it is done by Composable :/
            // Didn't find a way how to extract the TextFieldInputState into the VM without reference to Compose and with the ability to use flows to combine.
            viewModel.updateCatPassword(it.toString())
        }.launchIn(viewModel.viewModelScope)
    }

    val selectedImage: Uri? by viewModel.catImageUri.collectAsStateWithLifecycle()
    val showPassword: Boolean by viewModel.showPassword.collectAsStateWithLifecycle()
    val hasAutoOpenedIme: Boolean by viewModel.hasAutoOpenedIme.collectAsStateWithLifecycle()
    val isContinueButtonEnabled: Boolean by viewModel.isContinueButtonEnabled.collectAsStateWithLifecycle()

    // Testing whether I can use complex Flow operators from Compose states. This works
    /* LaunchedEffect(Unit) {
         combine(snapshotFlow { hasAutoOpenedIme }, snapshotFlow { showPassword }) {
             hasAutoOpenedIme, showPassword ->
             log("hasAutoOpenedIme: $hasAutoOpenedIme, showPassword: $showPassword")
         }.launchIn(scope)
     }*/

    val catNameBringIntoViewRequester = remember { BringIntoViewRequester() }
    val passwordBringIntoViewRequester = remember { BringIntoViewRequester() }

    DefaultSurface {
        // Text input 1
        DefaultScrollableColumn(scrollState = scrollState) {
            // region OutlinedTextField

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Cat name:")
                Text(
                    text = "Outlined Text Field",
                    modifier = Modifier.weight(1.0f),
                    textAlign = TextAlign.End,
                    fontSize = 12.sp
                )
            }
            SpacerVertical(height = smallGap)

            OutlinedTextField(
                value = catNameInput,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(catNameFieldFocusRequester)
                    .clearFocusOnKeyboardDismiss()
                    .newBringIntoViewRequesterModifier(catNameBringIntoViewRequester, scope),
                onValueChange = {
                    viewModel.updateCatName(it)
                },
                label = { Text("Enter cat's name") },
                placeholder = { Text(text = "Here (placeholder test)") },
                suffix = { Text("Suffix test") },
                singleLine = true,
                // By default it seems it hides keyboard if singleLine = true
                keyboardActions = KeyboardActions(onDone = {
                    //  keyboardController?.hide() - this works
                    //  catNameFieldFocusRequester.freeFocus() - not working
                    focusManager.clearFocus() // Not needed, seems this is native behaviour, but keeping it here for documentation.
                })
            )
            SpacerVertical(height = smallGap)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Cat password (numeric):")
                Text(
                    text = "Basic Secure Text Field",
                    modifier = Modifier.weight(1.0f),
                    textAlign = TextAlign.End,
                    fontSize = 12.sp
                )
            }
            SpacerVertical(height = mediumGap)

            // endregion
            // region BasicSecureTextField

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.CenterStart, modifier = Modifier
                        .weight(1.0f)
                ) {
                    /*
                    * TODO: I was considering changing the symbol used for hiding characters.
                    *  Seems to be \u2022. But it is not a parameter. It's hardcoded inside of the BasicSecureTextField
                    * and changing it would require custom input transformation or codepointTransformation
                    * used internally in BasicSecureTextField. But it shouldn't be too hard.
                    * TODO: There are some bugs with this text field. Like if user manually closes the keyboard
                    *  it won't reappear (strangely enough, changing textObfuscationMode will make it appear).
                    * They claim it has been fixed: https://issuetracker.google.com/issues/312895384 in compose
                    * versions above 1.6.0, but it doesn't seem to be the case at least for the BasicSecureTextField.
                    * */
                    BasicSecureTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .newBringIntoViewRequesterModifier(passwordBringIntoViewRequester, scope)
                            .clearFocusOnKeyboardDismiss()
                            .padding(16.dp),
                        state = passwordState,
                        textObfuscationMode = when (showPassword) {
                            true -> TextObfuscationMode.Visible
                            false -> TextObfuscationMode.RevealLastTyped
                        },
                        keyboardType = KeyboardType.NumberPassword
                    ) // Based on BasicTextField2

                    if (passwordState.text.isEmpty()) {
                        Text(
                            text = "Input cat's password",
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Outlined.Build,// I didn't find a default eye icon :D TODO() import custom
                    contentDescription = null,
                    tint = if (!showPassword) Color.Black else Color.Red,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false), // You can also change the color and radius of the ripple
                            onClick = {
                                viewModel.switchPasswordVisibility()
                            }
                        )
                        .padding(8.dp)
                )
            }

            // endregion
            // TODO() add state.undoState methods
            SpacerVertical(height = smallGap)
            PhotoSelectorView(maxSelectionCount = 1, onResult = { images ->
                viewModel.updateCatImageUri(images.firstOrNull())
            })
            SpacerVertical(height = smallGap)
            // https://stackoverflow.com/questions/68166660/jetpack-compose-alternative-to-view-systems-animatelayoutchanges
            AnimatedVisibility(visible = selectedImage != null) {
                ImageLayoutView(
                    selectedImages = listOf(selectedImage),
                    imageModifier = Modifier
                        .bounceClickEffect()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            openRemoveImageAlertDialog.value = true
                        }
                )
            }

            SpacerVertical(height = smallGap)

            (0..40).forEach {
                Text("Scrolling Pojo $it")
            }

            // Bottom padding because of the button
            SpacerVertical(height = 80.dp)
        }
    }
    // region ImePushUp button
    ImePushBox {
        HorizontalDivider()
        AnimatedButton(
            text = "Continue",
            enabled = isContinueButtonEnabled,
            onClick = {
                Toast.makeText(context, "Works", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )
    }

    if (openRemoveImageAlertDialog.value) {
        SimpleAlertDialog(
            onDismissRequest = {
                openRemoveImageAlertDialog.value = false
            },
            onConfirmation = {
                viewModel.removeSelectedImageUri()
                openRemoveImageAlertDialog.value = false
            },
            dialogTitle = "Wanna remove the image?",
            dialogText = "If you remove it, it will be removed (surprise, yeah). More text?",
            icon = Icons.Default.Clear
        )
    }
    /*
        fun openSheet() {
            val cat = PlaygroundCat(
                catNameInput,
                password.text.toString(),
                selectedImages.first() ?: return
            )
            isSheetOpen = true
        }

        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        // TODO: This should probably be done in ViewModel
        if (isSheetOpen) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isSheetOpen = false }) {
                PlaygroundCatContent(cat = )
            }
        }*/

    LaunchedEffect(Unit) {
        if (!hasAutoOpenedIme) {
            awaitFrame() // TODO() Figure out why I need delay
            catNameFieldFocusRequester.requestFocus()
            keyboardController?.show()
            viewModel.setHasAutoOpenedIme(true)
        }
    }
}

@Preview
@Composable
fun NavigationSamplePassArgumentsScreenPreview() {
    ComposePlaygroundTheme {
        NavigationSamplePassArgumentsScreen()
    }
}