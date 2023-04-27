package com.app.crudsqlitesample.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.app.crudsqlitesample.models.Contacts
import java.util.*
class SqliteDatabase internal constructor(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        val createContactTable = ("CREATE TABLE "
                + TABLE_CONTACTS + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_NO + " INTEGER" + ")")
        db.execSQL(createContactTable)
    }
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }
    fun listContacts(): ArrayList<Contacts> {
        val sql = "select * from $TABLE_CONTACTS"
        val db = this.readableDatabase
        val storeContacts =
            ArrayList<Contacts>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0).toInt()
                val name = cursor.getString(1)
                val phno = cursor.getString(2)
                storeContacts.add(Contacts(id, name, phno))
            }
            while (cursor.moveToNext())
        }
        cursor.close()
        return storeContacts
    }
    fun addContacts(contacts: Contacts) {
        val values = ContentValues()
        values.put(COLUMN_NAME, contacts.name)
        values.put(COLUMN_NO, contacts.phno)
        val db = this.writableDatabase
        db.insert(TABLE_CONTACTS, null, values)
    }
    fun updateContacts(contacts: Contacts) {
        val values = ContentValues()
        values.put(COLUMN_NAME, contacts.name)
        values.put(COLUMN_NO, contacts.phno)
        val db = this.writableDatabase
        db.update(
            TABLE_CONTACTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(contacts.id.toString())
        )
    }
    fun deleteContact(id: Int) {
        val db = this.writableDatabase
        db.delete(
            TABLE_CONTACTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }
    companion object {
        private const val DATABASE_VERSION = 5
        private const val DATABASE_NAME = "Contacts"
        private const val TABLE_CONTACTS = "Contacts"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "contactName"
        private const val COLUMN_NO = "phoneNumber"
    }
}