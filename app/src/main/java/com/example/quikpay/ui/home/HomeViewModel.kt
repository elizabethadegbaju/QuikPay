package com.example.quikpay.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.ProgressListener
import com.example.quikpay.data.models.Transaction
import com.example.quikpay.data.models.User
import com.example.quikpay.data.repositories.TransactionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {
    private val disposables = CompositeDisposable()
    var progressListener: ProgressListener? = null

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

    private var _navigateToViewOlder = MutableLiveData<Boolean>()
    val navigateToViewOlder: LiveData<Boolean>
        get() = _navigateToViewOlder

    private var _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private var _navigateToTopUp = MutableLiveData<Boolean>()
    val navigateToTopUp: LiveData<Boolean>
        get() = _navigateToTopUp

    private var _currentTab = MutableLiveData<String>()
    val currentTab: LiveData<String>
        get() = _currentTab

    private var _startTopUp = MutableLiveData<Boolean>()
    val startTopUp: LiveData<Boolean>
        get() = _startTopUp

    fun navigateToViewOlder() {
        _navigateToViewOlder.value = true
    }

    fun onNavigateToViewOlder() {
        _navigateToViewOlder.value = false
    }

    fun navigateToTopUp() {
        _navigateToTopUp.value = true
    }

    fun onNavigateToTopUp() {
        _navigateToTopUp.value = false
    }

    private fun navigateToHome() {
        _navigateToHome.value = true
    }

    fun onNavigateToHome() {
        _navigateToHome.value = false
    }

    fun showDrawer() {
        _showNavDrawer.value = true
    }

    fun doneShowNavDrawer() {
        _showNavDrawer.value = false
    }

    fun fetchUserDetails() {
        progressListener?.onStarted()
        val disposable = transactionRepository.fetchUserDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userDetails.value = transactionRepository.currentUserDetails()
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
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
        progressListener?.onStarted()
        val disposable = transactionRepository.updateReceivedHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _receivedTransactions.value = transactionRepository.receivedHistory()
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun updateSentTransactions() {
        progressListener?.onStarted()
        val disposable = transactionRepository.updateSentHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _sentTransactions.value = transactionRepository.sentHistory()
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun setCurrentTab(tab: String) {
        _currentTab.value = tab
    }

    fun startTopUp() {
        _startTopUp.value = true
    }

    fun topUp(amount: String) {
        _startTopUp.value = false
        progressListener?.onStarted()
        val disposable = transactionRepository.updateAccountBal(amount.toDouble())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                navigateToHome()
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }
}