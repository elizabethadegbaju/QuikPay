package com.example.quikpay.data.repositories

import com.example.quikpay.utils.FirebaseSource

class TransactionRepository(private val firebase: FirebaseSource) {

    fun fetchUserDetails() = firebase.fetchUserDetails()

    fun currentUserDetails() = firebase.userDetails

    fun updateSentHistory() = firebase.updateSentHistory()

    fun updateReceivedHistory() = firebase.updateReceivedHistory()

    fun sentHistory() = firebase.sentTransactions

    fun updateAccountBal(amount: Double) = firebase.updateAccountBal(amount)

    fun receivedHistory() = firebase.receivedTransactions

}