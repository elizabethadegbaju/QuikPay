package com.example.quikpay.ui.sendmoney

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
import kotlin.random.Random

class SendMoneyViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {
    private val TAG = SendMoneyViewModel::class.java.simpleName

    private val disposables = CompositeDisposable()
    var progressListener: ProgressListener? = null

    private var _quikpayAccNo = MutableLiveData<String>()
    val quikpayAccNo: LiveData<String>
        get() = _quikpayAccNo

    private var _bankAccNo = MutableLiveData<String>()
    val bankAccNo: LiveData<String>
        get() = _bankAccNo

    private var _bank = MutableLiveData<String>()
    val bank: LiveData<String>
        get() = _bank

    private var _amount = MutableLiveData<Double>()
    val amount: LiveData<Double>
        get() = _amount

    private var _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    private var _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private var _startSendMoneyQuikpay = MutableLiveData<Boolean>()
    val startSendMoneyQuikpay: LiveData<Boolean>
        get() = _startSendMoneyQuikpay

    private var _startSendMoneyBank = MutableLiveData<Boolean>()
    val startSendMoneyBank: LiveData<Boolean>
        get() = _startSendMoneyBank

    fun startSendMoneyQuikpay() {
        _startSendMoneyQuikpay.value = true
    }

    fun startSendMoneyBank() {
        _startSendMoneyBank.value = true
    }

    fun sendMoneyQuikpay() {
        _startSendMoneyQuikpay.value = false
        progressListener?.onStarted()
        val disposable = transactionRepository.findUser(quikpayAccNo.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ document ->
                val quikpayUser = document?.toObject(User::class.java)
                val quikpayUserUid = document?.id
                updateAccountBalance(quikpayUser!!, quikpayUserUid!!)
                createTransactions(quikpayUser, quikpayUserUid)
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun sendMoneyBank() {
        _startSendMoneyBank.value = false
        progressListener?.onStarted()
        val rand = Random(System.currentTimeMillis())
        val refNo = (10000000000..99999999999).random(rand).toString()
        val fromTransaction = Transaction(
            type = "from",
            other_user = "${bankAccNo.value} ${bank.value}",
            ref_number = refNo,
            description = description.value!!,
            amount = amount.value!!
        )
        val disposable =
            transactionRepository.updateAccountBal(-amount.value!!)
                .andThen(transactionRepository.createTransaction(fromTransaction))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressListener?.onSuccess()
                }, {
                    progressListener?.onFailure(it.message!!)
                })
        disposables.add(disposable)
    }

    fun setValues(
        accNo: String = "",
        amount: Double,
        description: String,
        type: String,
        bank: String = ""
    ) {
        when (type) {
            "quikpay" -> _quikpayAccNo.value = accNo
            "bank" -> {
                _bankAccNo.value = accNo
                _bank.value = bank
            }
        }
        _amount.value = amount
        _description.value = description
    }

    private fun updateAccountBalance(user: User, uid: String) {
        val disposable =
            transactionRepository.updateAccountBal(amount.value!!, user.accountBal, uid)
                .andThen(transactionRepository.updateAccountBal(-amount.value!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    progressListener?.onFailure(it.message!!)
                })
        disposables.add(disposable)
    }

    private fun createTransactions(user: User, uid: String) {
        val rand = Random(System.currentTimeMillis())
        val refNo = (10000000000..99999999999).random(rand).toString()
        val toTransaction = Transaction(
            type = "to",
            other_user = transactionRepository.currentUserDetails().name,
            ref_number = refNo,
            description = description.value!!,
            amount = amount.value!!
        )
        val fromTransaction = Transaction(
            type = "from",
            other_user = user.name,
            ref_number = refNo,
            description = description.value!!,
            amount = amount.value!!
        )
        val disposable =
            transactionRepository.createTransaction(toTransaction, uid)
                .andThen(transactionRepository.createTransaction(fromTransaction))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    progressListener?.onFailure(it.message!!)
                })
        disposables.add(disposable)
    }

    fun onNavigateToHome() {
        _navigateToHome.value = false
    }
}