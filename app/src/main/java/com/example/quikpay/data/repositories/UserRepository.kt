package com.example.quikpay.data.repositories

import android.net.Uri
import com.example.quikpay.data.repositories.firebase.FirebaseSource

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

    fun logout() = firebase.logout()
    fun uploadFile(filePath: Uri, phoneNo: String) = firebase.uploadFile(filePath, phoneNo)
}