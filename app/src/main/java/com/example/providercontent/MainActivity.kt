package com.example.providercontent

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contentprovider2.MyAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var adapter: MyAdapter

    private val originalItems = mutableListOf<ContactModal>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this,originalItems)
        recyclerView.adapter = adapter

        if (checkContactsPermission()) {
            loadContacts()
        } else {
            requestContactsPermission()
        }

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                performSearch(newText)
                return true
            }
        })
    }

    private fun checkContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_CONTACTS),
            REQUEST_CONTACTS_PERMISSION
        )
    }

    @SuppressLint("Range")
    private fun loadContacts() {
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val contactName =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                val contactNumber =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).toString()
                        .replace(" ","").replace("+91","")
                val image = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                if (!originalItems.contains(ContactModal(contactName, contactNumber,image)))
                {
                    originalItems.add(ContactModal( contactName, contactNumber,image))
                }
            }
        }

        cursor?.close()
        adapter.updateData(originalItems)
    }

    private fun performSearch(query: String?) {
        val filteredList = originalItems.filter {
            it.name.contains(query.orEmpty(), ignoreCase = true) ||
                    it.phoneNumber!!.contains(query.orEmpty(), ignoreCase = true)
        }
        adapter.updateData(filteredList)
    }

    companion object {
        private const val REQUEST_CONTACTS_PERMISSION = 123
    }
}