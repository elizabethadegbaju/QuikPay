package com.example.quikpay.ui.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.R
import com.example.quikpay.data.models.Contact
import com.example.quikpay.databinding.FragmentContactsBinding
import com.google.gson.Gson


class ContactsFragment : Fragment() {
    private lateinit var selectUsers: MutableList<Contact>
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentContactsBinding
    private lateinit var phones: Cursor
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        recyclerView = binding.list
        binding.lifecycleOwner = this

        selectUsers = mutableListOf()
        showContacts()
        return binding.root
    }

    private fun showContacts() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            phones = requireContext().contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )!!
            val loadContact = LoadContact()
            loadContact.execute()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts()
            } else {
                Toast.makeText(
                    context,
                    "Until you grant the permission, we cannot display the names",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoadContact : AsyncTask<Any, Any, Any>() {
        override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)
            val removed: MutableList<Contact> = ArrayList()
            val contacts: MutableList<Contact> = ArrayList()
            for (i in selectUsers.indices) {
                val friend = selectUsers[i]
                if (friend.name.matches(Regex("\\d+(?:\\.\\d+)?")) || friend.name.trim().isEmpty()
                ) {
                    removed.add(friend)
                    Log.d("Removed Contact", Gson().toJson(friend))
                } else {
                    contacts.add(friend)
                }
            }
            contacts.addAll(removed)
            selectUsers = contacts

            with(recyclerView) {
                layoutManager = LinearLayoutManager(context)
                adapter = ContactsViewAdapter()
                (adapter as ContactsViewAdapter).submitList(selectUsers)
            }
        }

        override fun doInBackground(vararg params: Any?): Any? {
            Log.e("count", "" + phones.count)

            while (phones.moveToNext()) {
                val photo =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
                val id =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val selectUser = Contact()
                selectUser.name = name
                selectUser.id = id
//                selectUser.photo = photo
                selectUser.phone = phoneNumber
                selectUsers.add(selectUser)
            }

            return null
        }

    }
}
