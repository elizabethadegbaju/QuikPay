package com.example.quikpay.ui.pool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.data.repositories.PoolRepository

@Suppress("UNCHECKED_CAST")
class PoolViewModelFactory(
    private val poolRepository: PoolRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PoolViewModel(poolRepository) as T
    }

}