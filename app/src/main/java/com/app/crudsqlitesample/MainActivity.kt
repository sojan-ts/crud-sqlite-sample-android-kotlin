package com.app.crudsqlitesample

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.crudsqlitesample.adapters.ContactAdapter
import com.app.crudsqlitesample.database.SqliteDatabase
import com.app.crudsqlitesample.models.Contacts
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dataBase: SqliteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "CRUD Sample"

        val contactView: RecyclerView = findViewById(R.id.myContactList)
        val linearLayoutManager = LinearLayoutManager(this)
        contactView.layoutManager = linearLayoutManager
        contactView.setHasFixedSize(true)

        dataBase = SqliteDatabase(this)
        val allContacts = dataBase.listContacts()
        if (allContacts.isNotEmpty()) {
            contactView.visibility = View.VISIBLE
            val mAdapter = ContactAdapter(this, allContacts)
            contactView.adapter = mAdapter
        } else {
            contactView.visibility = View.GONE
            Toast.makeText(
                this,
                "There is no contact in the database. Start adding now",
                Toast.LENGTH_LONG
            ).show()
        }

        val btnAdd: FloatingActionButton = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener { addTaskDialog() }
    }

    private fun addTaskDialog() {
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.add_contacts, null)
        val nameField: EditText = subView.findViewById(R.id.enterName)
        val noField: EditText = subView.findViewById(R.id.enterPhoneNum)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new CONTACT")
        builder.setView(subView)
        builder.setPositiveButton("ADD CONTACT") { _, _ ->
            val name = nameField.text.toString()
            val phoneNum = noField.text.toString()
            val newContact: Contacts // Define newContact variable here
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(
                    this@MainActivity,
                    "Something went wrong. Check your input values",
                    Toast.LENGTH_LONG
                ).show()
            } else {
//                val newContact = Contacts(name, phoneNum)
//                dataBase.addContacts(newContact)
//                finish()
//                startActivity(intent)
                newContact = Contacts(name, phoneNum) // Initialize newContact here
                dataBase.addContacts(newContact)
                val allContacts = dataBase.listContacts()
                if (allContacts.isNotEmpty()) {
                    val contactView: RecyclerView = findViewById(R.id.myContactList)
                    contactView.visibility = View.VISIBLE
                    val mAdapter = ContactAdapter(this, allContacts)
                    contactView.adapter = mAdapter
                }
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            Toast.makeText(this@MainActivity, "Task cancelled", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        dataBase.close()
    }
}
