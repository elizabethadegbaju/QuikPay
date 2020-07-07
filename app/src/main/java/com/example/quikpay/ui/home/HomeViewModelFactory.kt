package com.example.quikpay.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.data.repositories.TransactionRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(transactionRepository) as T
    }

}