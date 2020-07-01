package com.example.quikpay.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Complaint(
    val message: String = "",
    val user: String = "",
    val date: String = "",
    val tz: String = "",
    @ServerTimestamp val submittedAt: Timestamp? = null
)