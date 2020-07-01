package com.example.quikpay.ui.reportissue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.data.repositories.GeneralRepository

@Suppress("UNCHECKED_CAST")
class ReportIssueViewModelFactory(
    private val generalRepository: GeneralRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReportIssueViewModel(generalRepository) as T
    }

}