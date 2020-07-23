package com.example.quikpay.ui.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.R
import com.example.quikpay.data.models.Contact
import com.example.quikpay.databinding.FragmentContactsBinding
import com.example.quikpay.ui.pool.PoolViewModel
import com.example.quikpay.ui.pool.PoolViewModelFactory
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ContactsFragment : Fragment(), ContactsViewAdapter.ViewHolder.ClickListener, KodeinAware {
    val TAG = "ContactsFragment"
    override val kodein by kodein()
    private val factory: PoolViewModelFactory by instance()
    private lateinit var poolViewModel: PoolViewModel
    private lateinit var contacts: MutableList<Contact>
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentContactsBinding
    private lateinit var phones: Cursor
    private lateinit var contactAdapter: ContactsViewAdapter
    private val actionModeCallback = ActionModeCallback()
    private var actionMode: ActionMode? = null
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "fragment created")
        poolViewModel = ViewModelProvider(requireActivity(), factory).get(PoolViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        recyclerView = binding.list
        binding.lifecycleOwner = activity

        contacts = mutableListOf()
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
            for (i in this@ContactsFragment.contacts.indices) {
                val friend = this@ContactsFragment.contacts[i]
                if (friend.name.matches(Regex("\\d+(?:\\.\\d+)?")) || friend.name.trim().isEmpty()
                ) {
                    removed.add(friend)
                    Log.d("Removed Contact", Gson().toJson(friend))
                } else {
                    contacts.add(friend)
                }
            }
            contacts.addAll(removed)
            this@ContactsFragment.contacts = contacts
            contactAdapter = ContactsViewAdapter(this@ContactsFragment)
            with(recyclerView) {
                layoutManager = LinearLayoutManager(context)
                adapter = contactAdapter
                (adapter as ContactsViewAdapter).submitList(this@ContactsFragment.contacts)
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
                val contact = Contact()
                contact.name = name
                contact.id = id
//                selectUser.photo = photo
                contact.phone = phoneNumber
                contacts.add(contact)
            }

            return null
        }

    }

    override fun onItemClicked(
        position: Int,
        contact: Contact
    ): Boolean {
        if (actionMode == null) {
            actionMode = requireActivity().startActionMode(actionModeCallback)
        }

        toggleSelection(position, contact)
        return true
    }

    private fun toggleSelection(
        position: Int,
        contact: Contact
    ) {
        contactAdapter.toggleSelection(position, contact)
        val count: Int = contactAdapter.selectedItemCount
        if (count == 0) {
            actionMode!!.finish()
        } else {
            actionMode!!.title = count.toString()
            actionMode!!.invalidate()
        }
    }

    private inner class ActionModeCallback : ActionMode.Callback {
        private val TAG = ActionModeCallback::class.java.simpleName
        override fun onCreateActionMode(
            mode: ActionMode,
            menu: Menu?
        ): Boolean {
            mode.menuInflater.inflate(R.menu.selected_menu, menu)
            return true
        }

        override fun onPrepareActionMode(
            mode: ActionMode?,
            menu: Menu?
        ): Boolean {
            return false
        }

        override fun onActionItemClicked(
            mode: ActionMode,
            item: MenuItem
        ): Boolean {
            return when (item.itemId) {
                R.id.menu_remove -> {
                    contactAdapter.clearSelection()
                    Log.d(TAG, "menu_remove")
                    true
                }
                R.id.menu_done -> {
                    mode.finish()
                    poolViewModel.selectContacts(contactAdapter.selectedContactList)
                    Navigation.findNavController(binding.root)
                        .navigateUp()
                    Log.d(TAG, "menu_done")
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            contactAdapter.clearSelection()
            actionMode = null
        }
    }
}
