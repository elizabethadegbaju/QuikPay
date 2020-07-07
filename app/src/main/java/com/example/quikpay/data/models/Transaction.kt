package com.example.quikpay.data.models

data class Transaction(
    val type: String = "",
    val other_user: String = "",
    val date: String = "",
    val ref_number: String = "",
    val description: String = "",
    val amount: Double = 0.00
)