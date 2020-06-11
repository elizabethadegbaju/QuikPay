package com.example.quikpay.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.R

class WelcomeViewModel : ViewModel() {

    private var _getStartedVisible = MutableLiveData<Boolean>()
    val getStartedVisible: LiveData<Boolean>
        get() = _getStartedVisible

    private var _nextCount: Int = 0

    private var _image = MutableLiveData<Int>()
    val image: LiveData<Int>
        get() = _image

    private var _introTitle = MutableLiveData<Int>()
    val introTitle: LiveData<Int>
        get() = _introTitle

    private var _introText = MutableLiveData<Int>()
    val introText: LiveData<Int>
        get() = _introText

    private var _square1 = MutableLiveData<Int>()
    val square1: LiveData<Int>
        get() = _square1

    private var _square2 = MutableLiveData<Int>()
    val square2: LiveData<Int>
        get() = _square2

    private var _square3 = MutableLiveData<Int>()
    val square3: LiveData<Int>
        get() = _square3

    init {
        _nextCount = 1
        _getStartedVisible.value = false
        showStep1()
    }

    private var _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun onNavigateToLogin() {
        _navigateToLogin.value = true
    }

    fun doneNavigating() {
        _navigateToLogin.value = false
    }

    fun next() {
        if (_nextCount < 3) {
            _nextCount += 1
        }
        when (_nextCount) {
            2 -> showStep2()
            3 -> {
                showStep3()
                showGetStarted()
            }
            else -> showStep1()
        }
    }

    private fun showGetStarted() {
        _getStartedVisible.value = true
    }

    private fun showStep1() {
        _image.value = R.drawable.group_3
        _introTitle.value = R.string.setup_money_pools
        _introText.value = R.string.setup_money_pools_intro
        _square1.value = R.drawable.ic_square_lightblue
        _square2.value = R.drawable.ic_square_white
        _square3.value = R.drawable.ic_square_white
    }

    private fun showStep2() {
        _image.value = R.drawable.group_4
        _introTitle.value = R.string.fund_wallet
        _introText.value = R.string.fund_wallet_intro
        _square1.value = R.drawable.ic_square_white
        _square2.value = R.drawable.ic_square_lightblue
        _square3.value = R.drawable.ic_square_white
    }

    private fun showStep3() {
        _image.value = R.drawable.group_5
        _introTitle.value = R.string.make_payment
        _introText.value = R.string.make_payment_intro
        _square1.value = R.drawable.ic_square_white
        _square2.value = R.drawable.ic_square_white
        _square3.value = R.drawable.ic_square_lightblue
    }
}
