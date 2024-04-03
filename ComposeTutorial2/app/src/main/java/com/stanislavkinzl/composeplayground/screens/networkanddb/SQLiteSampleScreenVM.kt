package com.stanislavkinzl.composeplayground.screens.networkanddb

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanislavkinzl.composeplayground.WhileViewSubscribed
import com.stanislavkinzl.composeplayground.data.Person
import com.stanislavkinzl.composeplayground.data.db.sqlite.SQLiteDBHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class SQLiteSampleScreenVM(
    private val sqLiteDBHelper: SQLiteDBHelper
) : ViewModel() {

    init {
        sqLiteDBHelper.refresh()
    }

    val people = sqLiteDBHelper.people.map {
        it.sortedBy { person -> person.age.toInt() }
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private val _personToRemove = MutableStateFlow<Person?>(null)
    val personToRemove = _personToRemove.asStateFlow()

    private val _inputName = MutableStateFlow("")
    val inputName = _inputName.asStateFlow()

    private val _inputAge = MutableStateFlow("")
    val inputAge = _inputAge.asStateFlow()

    val insertButtonEnabled = combine(inputName, inputAge) { inputName, inputAge ->
        inputName.isNotEmpty() && inputAge.isNotEmpty()
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    fun updateInputName(updatedInput: String) {
        _inputName.value = updatedInput
    }

    fun updateInputAge(updatedInput: String) {
        if (updatedInput.isDigitsOnly() && updatedInput.length < 3) {
            _inputAge.value = updatedInput
        }
    }

    fun insertPersonIntoDB() {
        sqLiteDBHelper.addPerson(
            Person(UUID.randomUUID().toString(), _inputName.value, _inputAge.value)
        ).also {
            _inputAge.value = ""
            _inputName.value = ""
        }
    }

    fun onPersonRemovalRequest(person: Person) {
        _personToRemove.value = person
    }

    fun onCancelPersonRemove() {
        _personToRemove.value = null
    }

    fun removePerson(person: Person) {
        sqLiteDBHelper.deletePerson(person.id)
        _personToRemove.value = null
    }
}