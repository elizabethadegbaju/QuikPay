package com.example.quikpay.ui.reportissue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.data.repositories.GeneralRepository
import com.example.quikpay.ui.authentication.ProgressListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class ReportIssueViewModel(
    private val repository: GeneralRepository
) : ViewModel() {

    var progressListener: ProgressListener? = null
    private val disposables = CompositeDisposable()

    private var _setValues = MutableLiveData<Boolean>()
    val setValues: LiveData<Boolean>
        get() = _setValues

    private val cal: Calendar by lazy { Calendar.getInstance() }
    private val simpleDateFormatDate by lazy {
        SimpleDateFormat(
            "E, MMM dd, yyyy",
            Locale.getDefault()
        )
    }

    fun reportIssue(issue: String) {
        progressListener?.onStarted()
        val date = cal.time
        val timezone = cal.timeZone.displayName!!
        val strDate = simpleDateFormatDate.format(date)
        val disposable =
            repository.reportIssue(issue, strDate, timezone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressListener?.onSuccess()
                }, {
                    progressListener?.onFailure("Unable to submit your complaint at the moment. Please try again after a short while.")
                })
        disposables.add(disposable)
    }

    fun submit() {
        _setValues.value = true
    }

    fun onSubmit() {
        _setValues.value = false
    }
}