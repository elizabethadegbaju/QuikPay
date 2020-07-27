package com.example.quikpay.data.repositories

import com.example.quikpay.data.models.Transaction
import com.example.quikpay.utils.FirebaseSource

class TransactionRepository(private val firebase: FirebaseSource) {

    fun fetchUserDetails() = firebase.fetchUserDetails()

    fun currentUserDetails() = firebase.userDetails

    fun updateSentHistory() = firebase.updateSentHistory()

    fun updateReceivedHistory() = firebase.updateReceivedHistory()

    fun sentHistory() = firebase.sentTransactions

    fun updateAccountBal(amount: Double) = firebase.updateAccountBal(amount)

    fun updateAccountBal(amount: Double, accountBal: Double, userUid: String) =
        firebase.updateAccountBal(amount, accountBal, userUid)

    fun findUser(accountNo: String) = firebase.findUser(accountNo)

    fun receivedHistory() = firebase.receivedTransactions

    fun createTransaction(transaction: Transaction, userUid: String) =
        firebase.createTransaction(transaction, userUid)

    fun createTransaction(transaction: Transaction) =
        firebase.createTransaction(transaction)
}