package com.example.quikpay.data.models

data class PoolRequest(
    val from_acc: String = "",
    val from_name: String = "",
    val to_phoneNo: String = "",
    val paid: Boolean = false,
    val amount: Double = 0.00
)