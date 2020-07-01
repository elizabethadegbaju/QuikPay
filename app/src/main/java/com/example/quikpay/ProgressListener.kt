package com.example.quikpay

interface ProgressListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}