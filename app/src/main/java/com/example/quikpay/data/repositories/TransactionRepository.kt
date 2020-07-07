package com.example.quikpay.data.repositories

import com.example.quikpay.data.repositories.firebase.FirebaseSource

class TransactionRepository(private val firebase: FirebaseSource) {

    fun fetchUserDetails() = firebase.fetchUserDetails()

    fun currentUserDetails() = firebase.userDetails

    fun updateSentHistory() = firebase.updateSentHistory()

    fun updateReceivedHistory() = firebase.updateReceivedHistory()

    fun sentHistory() = firebase.sentTransactions

    fun receivedHistory() = firebase.receivedTransactions

}