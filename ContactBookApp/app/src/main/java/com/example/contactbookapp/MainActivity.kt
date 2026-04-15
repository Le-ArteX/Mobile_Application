package com.example.contactbookapp

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var tvEmptyState: TextView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var adapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        searchView = findViewById(R.id.searchView)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        fabAdd = findViewById(R.id.fabAdd)

        // Initial Data
        contactList.add(Contact("John Doe", "123456789", "john@email.com", "J"))
        contactList.add(Contact("Jane Smith", "987654321", "jane@email.com", "J"))

        adapter = ContactAdapter(this, contactList)
        listView.adapter = adapter

        updateEmptyState()

        // FAB Click
        fabAdd.setOnClickListener {
            showAddContactDialog()
        }

        // ListView Item Click
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val contact = adapter.getItem(position)
            if (contact != null) {
                Toast.makeText(this, "Name: ${contact.name}\nPhone: ${contact.phone}\nEmail: ${contact.email}", Toast.LENGTH_LONG).show()
            }
        }

        // ListView Long Press
        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val contact = adapter.getItem(position)
            if (contact != null) {
                showDeleteConfirmationDialog(contact)
            }
            true
        }

        // SearchView Filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText) {
                    updateEmptyState()
                }
                return true
            }
        })
    }

    private fun showAddContactDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etPhone)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        AlertDialog.Builder(this)
            .setTitle("Add New Contact")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                val email = etEmail.text.toString().trim()

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                    val initial = if (name.isNotEmpty()) name[0].uppercaseChar().toString() else "C"
                    val newContact = Contact(name, phone, email, initial)
                    contactList.add(newContact)
                    adapter.notifyDataSetChanged()
                    listView.setSelection(contactList.size - 1)
                    updateEmptyState()
                    Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Name and Phone are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete ${contact.name}?")
            .setPositiveButton("Delete") { _, _ ->
                contactList.remove(contact)
                adapter.notifyDataSetChanged()
                updateEmptyState()
                Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateEmptyState() {
        if (adapter.count == 0) {
            tvEmptyState.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }
}