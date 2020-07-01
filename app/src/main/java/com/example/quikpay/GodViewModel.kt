package com.example.quikpay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.data.repositories.UserRepository

class GodViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private var _navigateToReportIssue = MutableLiveData<Boolean>()
    val navigateToReportIssue: LiveData<Boolean>
        get() = _navigateToReportIssue

    fun logout() {
        userRepository.logout()
        _navigateToLogin.value = true
    }

    fun reportAnIssue() {
        _navigateToReportIssue.value = true
    }

    fun onNavigateToReportIssue() {
        _navigateToReportIssue.value = false
    }

    fun onNavigateToLogin() {
        _navigateToLogin.value = false
    }
}