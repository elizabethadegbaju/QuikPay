package com.example.quikpay.data.repositories

import com.example.quikpay.utils.FirebaseSource

class GeneralRepository(
    private val firebase: FirebaseSource
) {
    fun reportIssue(message: String, strDate: String, timezone: String) =
        firebase.reportIssue(message, strDate, timezone)

    fun logout() = firebase.logout()

    fun currentUserDetails() = firebase.userDetails

    fun currentUser() = firebase.currentUser()

    fun fetchUserDetails() = firebase.fetchUserDetails()
}