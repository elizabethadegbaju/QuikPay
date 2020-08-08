package com.example.quikpay.ui.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quikpay.ProgressListener
import com.example.quikpay.data.models.User
import com.example.quikpay.data.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val TAG = EditProfileViewModel::class.java.simpleName

    private val disposables = CompositeDisposable()
    var progressListener: ProgressListener? = null

    private var _userDetails = MutableLiveData<User>()
    val userDetails: LiveData<User>
        get() = _userDetails

    private var _uploadPhoto = MutableLiveData<Boolean>()
    val uploadPhoto: LiveData<Boolean>
        get() = _uploadPhoto

    private var _startUpdate = MutableLiveData<Boolean>()
    val startUpdate: LiveData<Boolean>
        get() = _startUpdate

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private var _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    private var _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private var _phoneNo = MutableLiveData<String>()
    val phoneNo: LiveData<String>
        get() = _phoneNo

    private var _filepath = MutableLiveData<Uri>()
    private val filePath: LiveData<Uri>
        get() = _filepath

    fun fetchUserDetails() {
        progressListener?.onStarted()
        val disposable = userRepository.fetchUserDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userDetails.value = userRepository.userDetails()
                progressListener?.onSuccess()
            }, {
                progressListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun startUpdate() {
        _startUpdate.value = true
    }

    fun updateUserDetails() {
        _startUpdate.value = false
        progressListener?.onStarted()
        val disposable =
            userRepository.uploadFile(filePath.value, phoneNo.value!!)
                .andThen(
                    userRepository.updateUserDetails(
                        name.value!!,
                        phoneNo.value!!,
                        email.value!!
                    )
                )
                .andThen(userRepository.updateEmail(email.value!!))
                .andThen(userRepository.updatePassword(password.value!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressListener?.onSuccess()
                }, {
                    progressListener?.onFailure(it.message!!)
                })
        disposables.add(disposable)
    }

    fun startUploadPhoto() {
        _uploadPhoto.value = true
    }

    fun doneUploadPhoto() {
        _uploadPhoto.value = false
    }

    fun setValues(
        name: String,
        phoneNo: String,
        filePath: Uri?,
        email: String,
        password: String
    ) {
        _name.value = name
        _phoneNo.value = phoneNo
        _filepath.value = filePath
        _email.value = email
        _password.value = password
    }
}
