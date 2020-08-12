package com.example.quikpay.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddContactViewModel() : ViewModel() {
    private val TAG = AddContactViewModel::class.java.simpleName

    private var _contactName = MutableLiveData<String>()
    val contactName: LiveData<String>
        get() = _contactName

    private var _contactPhoneNo = MutableLiveData<String>()
    val contactPhoneNo: LiveData<String>
        get() = _contactPhoneNo

    private var _contactEmail = MutableLiveData<String>()
    val contactEmail: LiveData<String>
        get() = _contactEmail

    private var _setValues = MutableLiveData<Boolean>()
    val setValues: LiveData<Boolean>
        get() = _setValues

    fun startAddContact() {
        _setValues.value = true
    }

    fun setContactValues(contactName: String, contactEmail: String, contactPhone: String) {
        _contactName.value = contactName
        _contactEmail.value = contactEmail
        _contactPhoneNo.value = contactPhone
    }
}