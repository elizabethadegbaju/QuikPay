package com.example.quikpay.ui.pool

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.ProgressListener
import com.example.quikpay.data.repositories.PoolRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class PoolViewModel(private val poolRepository: PoolRepository) : ViewModel() {
    private val TAG = "PoolViewModel"

    private val disposables = CompositeDisposable()
    var progressListener: ProgressListener? = null

    private var _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    private var _participants = MutableLiveData<List<String>>()
    val participants: LiveData<List<String>>
        get() = _participants

    private var _target = MutableLiveData<Double>()
    val target: LiveData<Double>
        get() = _target

    private var _startCreatePool = MutableLiveData<Boolean>()
    val startCreatePool: LiveData<Boolean>
        get() = _startCreatePool

    fun createPool() {
        _startCreatePool.value = false
        progressListener?.onStarted()
        Log.d(TAG, "I just started create pool")
        val disposable =
            poolRepository.createPool(description.value!!, target = target.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "I just finished create pool")
                    progressListener?.onSuccess()
                }, {
                    Log.d(TAG, "I couldn't finish create pool")
                    progressListener?.onFailure(it.message!!)
                })
        disposables.add(disposable)
    }

    fun startCreatePool() {
        _startCreatePool.value = true
    }

    fun setValues(description: String, target: String) {
        _description.value = description
        _target.value = target.toDouble()
    }
}