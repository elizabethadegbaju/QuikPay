package com.example.quikpay.ui.authentication.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForgotPasswordViewModel : ViewModel() {
    private var _navigateToVerification = MutableLiveData<Boolean>()
    val navigateToVerification: LiveData<Boolean>
        get() = _navigateToVerification

    private var _navigateToNewPassword = MutableLiveData<Boolean>()
    val navigateToNewPassword: LiveData<Boolean>
        get() = _navigateToNewPassword


    private var _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun submitEmail() {
        _navigateToVerification.value = true
    }

    fun onClickLogin() {
        _navigateToLogin.value = true
    }

    fun onClickNewPassword() {
        _navigateToNewPassword.value = true
    }

    fun doneNavigating() {
        _navigateToVerification.value = false
        _navigateToNewPassword.value = false
        _navigateToLogin.value = false
    }
}
