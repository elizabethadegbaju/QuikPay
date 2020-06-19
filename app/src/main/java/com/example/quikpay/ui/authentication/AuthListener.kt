package com.example.quikpay.ui.authentication

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}