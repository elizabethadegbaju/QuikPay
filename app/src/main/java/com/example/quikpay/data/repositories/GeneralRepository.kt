package com.example.quikpay.data.repositories

import com.example.quikpay.data.repositories.firebase.FirebaseSource

class GeneralRepository(
    private val firebase: FirebaseSource
) {
    fun reportIssue(message: String, strDate: String, timezone: String) =
        firebase.reportIssue(message, strDate, timezone)
}