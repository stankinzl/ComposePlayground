package com.stanislavkinzl.composeplayground.screens.networkanddb

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.stanislavkinzl.composeplayground.data.Person
import com.stanislavkinzl.composeplayground.ui.DefaultSurface
import com.stanislavkinzl.composeplayground.ui.SpacerVertical
import com.stanislavkinzl.composeplayground.ui.composables.AnimatedButton
import com.stanislavkinzl.composeplayground.ui.composables.ImePushBox
import com.stanislavkinzl.composeplayground.ui.composables.SimpleAlertDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SQLiteSampleScreen(
    viewModel: SQLiteSampleScreenVM = koinViewModel()
) {
    val inputName: String by viewModel.inputName.collectAsStateWithLifecycle()
    val inputAge: String by viewModel.inputAge.collectAsStateWithLifecycle()
    val insertButtonEnabled: Boolean by viewModel.insertButtonEnabled.collectAsStateWithLifecycle()
    val people: List<Person> by viewModel.people.collectAsStateWithLifecycle()
    val personToRemove: Person? by viewModel.personToRemove.collectAsStateWithLifecycle()

    DefaultSurface {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 16.dp)
        ) {
            item(key = "name_input") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth()
                        .animateItemPlacement(),
                    value = inputName,
                    onValueChange = {
                        viewModel.updateInputName(it)
                    },
                    label = {
                        Text("Enter person's name")
                    })
            }
            item(key = "age_input") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth()
                        .animateItemPlacement(),
                    value = inputAge,
                    onValueChange = {
                        viewModel.updateInputAge(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    label = {
                        Text("Enter person's age")
                    })
            }
            item(key = "db_list_title") {
                Text("List of people from DB sorted by age (why not)",
                    modifier = Modifier.padding(vertical = 16.dp)
                        .animateItemPlacement())
            }
            itemsIndexed(
                items = people,
                key = { _, person -> person.id }
            ) { index, person ->
                Box(modifier = Modifier.animateItemPlacement()) {
                    if (index != 0) {
                        HorizontalDivider()
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = true),
                                onClick = {
                                    viewModel.onPersonRemovalRequest(person)
                                }
                            )
                            .padding(8.dp),
                        text = "${person.name}, ${person.age}"
                    )
                }
            }
            item(key = "spacer_bottom") {
                SpacerVertical(height = 64.dp)
            }
        }
    }

    ImePushBox {
        HorizontalDivider()
        AnimatedButton(
            enabled = insertButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = {
                viewModel.insertPersonIntoDB()
            },
            text = "Insert"
        )
    }

    personToRemove?.let {
        SimpleAlertDialog(
            onDismissRequest = {
                viewModel.onCancelPersonRemove()
            },
            onConfirmation = {
                viewModel.removePerson(it)
            },
            dialogTitle = "Wanna remove the person from DB?",
            dialogText = "If you remove it, it will be removed (surprise, yeah).",
            icon = Icons.Default.Clear
        )
    }
}