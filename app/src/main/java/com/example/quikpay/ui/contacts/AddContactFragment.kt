package com.example.quikpay.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentAddContactBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddContactFragment : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private lateinit var addContactViewModel: AddContactViewModel

    private var contactName: String? = null
    private var contactEmail: String? = null
    private var contactPhone: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_contact, container, false)
        addContactViewModel = ViewModelProvider(this).get(AddContactViewModel::class.java)
        binding.addContactViewModel = addContactViewModel
        binding.lifecycleOwner = this

        addContactViewModel.setValues.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                contactName = binding.textName.text.toString()
                contactPhone = binding.textPhoneNo.text.toString()
                contactEmail = binding.textEmail.text.toString()
                addContact()
            }
        })

        return binding.root
    }

    private fun addContact() {
        // Creates a new Intent to insert a contact
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            // Sets the MIME type to match the Contacts Provider
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }
        /**
         * Inserts new data into the Intent. This data is passed to the
         * contacts app's Insert screen
         */
        intent.apply {
            // Inserts an email address
            contactEmail?.let {
                putExtra(ContactsContract.Intents.Insert.EMAIL, it)
                putExtra(
                    ContactsContract.Intents.Insert.EMAIL_TYPE,
                    ContactsContract.CommonDataKinds.Email.TYPE_WORK
                )
            }
            // Inserts a phone number
            contactPhone?.let {
                putExtra(ContactsContract.Intents.Insert.PHONE, it)
                putExtra(
                    ContactsContract.Intents.Insert.PHONE_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                )
            }
            //Inserts a name
            contactName?.let {
                putExtra(ContactsContract.Intents.Insert.NAME, it)
            }
        }
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AddContactFragment.
         */
        @JvmStatic
        fun newInstance() =
            AddContactFragment()
    }
}