package com.example.quikpay.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Transaction(
    val type: String = "",
    val other_user: String = "",
    @ServerTimestamp val date: Timestamp? = null,
    val ref_number: String = "",
    val description: String = "",
    val amount: Double = 0.00
)