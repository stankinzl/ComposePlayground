package com.stanislavkinzl.composeplayground.data.db.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.stanislavkinzl.composeplayground.data.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SQLiteDBHelper(
    context: Context,
    factory: SQLiteDatabase.CursorFactory?
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    private val _people = MutableStateFlow<List<Person>>(emptyList())
    val people = _people.asStateFlow()

    fun refresh() {
        _people.value = getAllPeople()
    }

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + PEOPLE_TABLE + " ("
                + ID_COL + " TEXT, " +
                NAME_COL + " TEXT," +
                AGE_COL + " TEXT" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $PEOPLE_TABLE")
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addPerson(person: Person) {
        val values = ContentValues()
        values.put(NAME_COL, person.name)
        values.put(AGE_COL, person.age)
        values.put(ID_COL, person.id)
        val db = this.writableDatabase
        db.insert(PEOPLE_TABLE, null, values)
        db.close()
        refresh()
    }

    fun deletePerson(id: String) {

        // on below line we are creating
        // a variable to write our database.
        val db = this.writableDatabase

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(PEOPLE_TABLE, "id=?", arrayOf(id))
        db.close()
        refresh()
    }

    // below method is to get
    // all data from our database
    @SuppressLint("Range")
    private fun getAllPeople(): List<Person> {
        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        val cursor = db.rawQuery("SELECT * FROM $PEOPLE_TABLE", null)
        val people = mutableListOf<Person>()

        if(!cursor!!.moveToFirst()) { // CursorIndexOutOfBoundsException: Index 0 requested, with a size of 0
            return emptyList()
        }

        val firstPersonName = cursor.getString(cursor.getColumnIndex(NAME_COL))
        val firstPersonAge = cursor.getString(cursor.getColumnIndex(AGE_COL))
        val firstPersonId = cursor.getString(cursor.getColumnIndex(ID_COL))
        people.add(Person(id = firstPersonId, name = firstPersonName, age = firstPersonAge))

        while (cursor.moveToNext()) {
            val personName = cursor.getString(cursor.getColumnIndex(NAME_COL))
            val personAge = cursor.getString(cursor.getColumnIndex(AGE_COL))
            val personId = cursor.getString(cursor.getColumnIndex(ID_COL))
            people.add(Person(id = personId, name = personName, age = personAge))
        }

        cursor.close()

        return people
    }

    companion object {
        private const val DATABASE_NAME = "SQLITE_DB"
        private const val DATABASE_VERSION = 1
        const val PEOPLE_TABLE = "people_table"
        const val ID_COL = "id"
        const val NAME_COL = "name"
        const val AGE_COL = "age"
    }
}