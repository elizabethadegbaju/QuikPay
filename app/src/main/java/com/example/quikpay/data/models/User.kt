package com.example.quikpay.data.models

data class User(
    val name: String = "",
    val email: String = "",
    val phoneNo: String = "",
    val photoURL: String = "",
    val accountNo: String = "0000000000",
    val accountBal: Double = 0.00
)