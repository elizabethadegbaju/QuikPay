package com.example.quikpay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.data.repositories.GeneralRepository

@Suppress("UNCHECKED_CAST")
class GodViewModelFactory(
    private val generalRepository: GeneralRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GodViewModel(generalRepository) as T
    }

}