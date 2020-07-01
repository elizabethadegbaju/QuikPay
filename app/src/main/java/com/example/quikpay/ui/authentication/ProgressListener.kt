package com.example.quikpay.ui.authentication

interface ProgressListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}