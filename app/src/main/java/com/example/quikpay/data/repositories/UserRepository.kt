package com.example.quikpay.data.repositories

import android.net.Uri
import com.example.quikpay.utils.FirebaseSource

class UserRepository(
    private val firebase: FirebaseSource
) {
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(
        email: String,
        password: String
    ) = firebase.register(email, password)

    fun currentUser() = firebase.currentUser()

    fun saveUserDetails(name: String, phoneNo: String) =
        firebase.saveUserDetails(name, phoneNo)

    fun updateUserDetails(name: String, phoneNo: String, email: String) =
        firebase.updateUserDetails(name, phoneNo, email)

    fun updateEmail(email: String) = firebase.updateEmail(email)

    fun updatePassword(password: String) = firebase.updatePassword(password)

    fun fetchUserDetails() = firebase.fetchUserDetails()

    fun userDetails() = firebase.userDetails

    fun uploadFile(filePath: Uri?, phoneNo: String) = firebase.uploadFile(filePath, phoneNo)

    fun reauthenticate(email: String, password: String) = firebase.reauthenticate(email, password)
}