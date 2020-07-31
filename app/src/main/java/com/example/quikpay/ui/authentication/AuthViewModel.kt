package com.example.quikpay.ui.authentication

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.ProgressListener
import com.example.quikpay.data.repositories.UserRepository
import com.example.quikpay.ui.authentication.forgotpassword.ForgotPasswordActivity
import com.example.quikpay.ui.authentication.login.LoginActivity
import com.example.quikpay.ui.authentication.signup.SignupActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var progressListener: ProgressListener? = null
    private val disposables = CompositeDisposable()

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private var _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    private var _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String>
        get() = _confirmPassword

    private var _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private var _phoneNo = MutableLiveData<String>()
    val phoneNo: LiveData<String>
        get() = _phoneNo

    private var _filepath = MutableLiveData<Uri>()
    private val filePath: LiveData<Uri>
        get() = _filepath

    private var _uploadPhoto = MutableLiveData<Boolean>()
    val uploadPhoto: LiveData<Boolean>
        get() = _uploadPhoto

    private var _navigateToPageTwo = MutableLiveData<Boolean>()
    val navigateToPageTwo: LiveData<Boolean>
        get() = _navigateToPageTwo

    private var _startLogin = MutableLiveData<Boolean>()
    val startLogin: LiveData<Boolean>
        get() = _startLogin

    private var _startSignup = MutableLiveData<Boolean>()
    val startSignup: LiveData<Boolean>
        get() = _startSignup

    val user by lazy {
        repository.currentUser()
    }

    fun login() {
        _startLogin.value = true
//        if (email.value.isNullOrEmpty() || password.value.isNullOrEmpty()) {
//            authListener?.onFailure("Invalid email or password")
//            return
//        }

        progressListener?.onStarted()

        val disposable = repository.login(email.value!!, password.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
        _startLogin.value = false
    }

    fun startSignupFun() {
        _startSignup.value = true
    }

    fun signup() {
        _startSignup.value = false
        progressListener?.onStarted()
        val disposable = repository.register(email.value!!, password.value!!)
            .andThen(repository.login(email.value!!, password.value!!))
            .andThen(repository.uploadFile(filePath.value!!, phoneNo.value!!))
            .andThen(repository.saveUserDetails(name.value!!, phoneNo.value!!))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun goToSignup(view: View) {
        Intent(view.context, SignupActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun goToSignupNextPage() {
        _navigateToPageTwo.value = true
    }

    fun doneNavigateToSignupNextPage() {
        _navigateToPageTwo.value = false
    }

    fun navigateToLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun navigateToForgotPassword(view: View) {
        Intent(view.context, ForgotPasswordActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun setSignupPageOneValues(name: String, phoneNo: String, filePath: Uri?) {
        _name.value = name
        _phoneNo.value = phoneNo.trim().removePrefix("+234").removePrefix("234").removePrefix("0")
        _filepath.value = filePath
    }

    fun setSignupPageTwoValues(email: String, password: String, confirmPassword: String) {
        setLoginValues(email, password)
        _confirmPassword.value = confirmPassword
    }

    fun setLoginValues(email: String, password: String) {
        _email.value = email
        _password.value = password
    }

    fun startUploadPhoto() {
        _uploadPhoto.value = true
    }

    fun doneUploadPhoto() {
        _uploadPhoto.value = false
    }

}