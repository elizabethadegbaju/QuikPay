package com.example.quikpay.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Pool(
    val created_by: String = "",
    val participants: List<String> = listOf(),
    val description: String = "",
    val target: Double = 0.00,
    @ServerTimestamp val created_at: Timestamp? = null
)