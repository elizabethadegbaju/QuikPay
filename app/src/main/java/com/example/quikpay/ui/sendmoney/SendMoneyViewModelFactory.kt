package com.example.quikpay.ui.sendmoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.data.repositories.TransactionRepository

@Suppress("UNCHECKED_CAST")
class SendMoneyViewModelFactory(
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SendMoneyViewModel(transactionRepository) as T
    }

}