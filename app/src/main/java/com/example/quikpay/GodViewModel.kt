package com.example.quikpay

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.data.models.User
import com.example.quikpay.data.repositories.GeneralRepository
import com.example.quikpay.utils.startLoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GodViewModel(
    private val generalRepository: GeneralRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()
    var progressListener: ProgressListener? = null

    private var _userDetails = MutableLiveData<User>(User())
    val userDetails: LiveData<User>
        get() = _userDetails

    private var _navigateToReportIssue = MutableLiveData<Boolean>()
    val navigateToReportIssue: LiveData<Boolean>
        get() = _navigateToReportIssue

    fun logout(view: View) {
        generalRepository.logout()
        view.context.startLoginActivity()
    }

    fun fetchUserDetails() {
        progressListener?.onStarted()
        val disposable = generalRepository.fetchUserDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _userDetails.value = generalRepository.currentUserDetails()
                progressListener?.onSuccess()
            }
        disposables.add(disposable)
    }

    fun reportAnIssue() {
        _navigateToReportIssue.value = true
    }

    fun onNavigateToReportIssue() {
        _navigateToReportIssue.value = false
    }
}