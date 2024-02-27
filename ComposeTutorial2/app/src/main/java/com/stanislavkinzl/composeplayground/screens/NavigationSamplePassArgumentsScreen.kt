package com.stanislavkinzl.composeplayground.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.input.TextObfuscationMode
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.stanislavkinzl.composeplayground.Global.mediumGap
import com.stanislavkinzl.composeplayground.Global.smallGap
import com.stanislavkinzl.composeplayground.getActivity
import com.stanislavkinzl.composeplayground.ui.DefaultScrollableColumn
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

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
/**
 * Playground for TextFields, auto-open IME, push on IME open
 * Keyboard tutorial: */
// https://blog.canopas.com/keyboard-handling-in-jetpack-compose-all-you-need-to-know-3e6fddd30d9a
// DialogFragment issues: https://patrykkosieradzki.com/how-to-fix-keyboard-issues-introduced-in-the-latest-jetpack-compose-1-4-0/
/** remember vs rememberSavable tutorial: */
// https://medium.com/@waghmaremayur855/understanding-the-difference-between-remember-and-remembersaveable-in-jetpack-compose-29d7231053e5#:~:text=remember%20is%20a%20fundamental%20part,value%20within%20a%20composable%20function.
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationSamplePassArgumentsScreen() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val catNameFieldFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var catNameInput: String by rememberSaveable { mutableStateOf("") }
    val password = rememberTextFieldState()
    var hasAutoOpenedIme: Boolean by rememberSaveable { mutableStateOf(false) }
    var showPassword: Boolean by rememberSaveable { mutableStateOf(false) }
    val catNameBringIntoViewRequester = remember { BringIntoViewRequester() }
    val passwordBringIntoViewRequester = remember { BringIntoViewRequester() }

    DefaultSurface {
        // Text input 1
        DefaultScrollableColumn(scrollState = scrollState) {
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
                    .newBringIntoViewRequesterModifier(catNameBringIntoViewRequester, scope),
                onValueChange = {
                    catNameInput = it
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
                            .padding(16.dp),
                        state = password,
                        textObfuscationMode = when (showPassword) {
                            true -> TextObfuscationMode.Visible
                            false -> TextObfuscationMode.RevealLastTyped
                        },
                        keyboardType = KeyboardType.NumberPassword
                    ) // Based on BasicTextField2

                    if (password.text.isEmpty()) {
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
                                showPassword = !showPassword
                            }
                        )
                        .padding(8.dp)
                )
            }

            // TODO() add state.undoState methods

            (0..40).forEach {
                Text("Scrolling Pojo $it")
            }

            // Bottom padding because of the button
            SpacerVertical(height = 80.dp)
        }
    }

    Box(contentAlignment = Alignment.BottomStart, modifier = Modifier
        .fillMaxSize()
        .safeContentPadding()
        .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HorizontalDivider()
            ElevatedButton(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), onClick = { /*TODO*/ }) {
                Text("Continue")
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!hasAutoOpenedIme) {
            awaitFrame() // TODO() Figure out why I need delay
            catNameFieldFocusRequester.requestFocus()
            keyboardController?.show()
            hasAutoOpenedIme = true
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