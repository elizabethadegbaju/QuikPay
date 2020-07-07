package com.example.quikpay.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.data.models.Transaction
import com.example.quikpay.data.models.User
import com.example.quikpay.data.repositories.TransactionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {
    private val disposables = CompositeDisposable()

    private var _userDetails = MutableLiveData<User>(User())
    val userDetails: LiveData<User>
        get() = _userDetails

    private var _allTransactions = MutableLiveData<List<Transaction>>()
    val allTransactions: LiveData<List<Transaction>>
        get() = _allTransactions

    private var _sentTransactions = MutableLiveData<MutableList<Transaction>>()
    val sentTransactions: LiveData<MutableList<Transaction>>
        get() = _sentTransactions

    private var _receivedTransactions = MutableLiveData<MutableList<Transaction>>()
    val receivedTransactions: LiveData<MutableList<Transaction>>
        get() = _receivedTransactions

    private var _showNavDrawer = MutableLiveData<Boolean>()
    val showNavDrawer: LiveData<Boolean>
        get() = _showNavDrawer

    fun showDrawer() {
        _showNavDrawer.value = true
    }

    fun doneShowNavDrawer() {
        _showNavDrawer.value = false
    }

    fun fetchUserDetails() {
        val disposable = transactionRepository.fetchUserDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _userDetails.value = transactionRepository.currentUserDetails()
            }
        disposables.add(disposable)
    }

    fun updateAllTransactions() {
        updateReceivedTransactions()
        updateSentTransactions()
        if (sentTransactions.value == null || receivedTransactions.value == null) {
            if (receivedTransactions.value != null) {
                _allTransactions.value = receivedTransactions.value
            } else if (sentTransactions.value != null) {
                _allTransactions.value = sentTransactions.value
            }
        } else {
            _allTransactions.value =
                _sentTransactions.value!!.plus(_receivedTransactions.value!!).distinct()
        }
    }

    fun updateReceivedTransactions() {
        val disposable = transactionRepository.updateReceivedHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _receivedTransactions.value = transactionRepository.receivedHistory()
            }
        disposables.add(disposable)
    }

    fun updateSentTransactions() {
        val disposable = transactionRepository.updateSentHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _sentTransactions.value = transactionRepository.sentHistory()
            }
        disposables.add(disposable)
    }
}