package com.example.quikpay.data.repositories.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable


class FirebaseSource {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val storageReference: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }

    private lateinit var photoURL: String

    fun currentUser() = auth.currentUser


    fun login(email: String, password: String) = Completable.create { emitter ->
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(
        email: String,
        password: String
    ) = Completable.create { emitter ->
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun logout() = auth.signOut()

    fun saveUserDetails(name: String, phoneNo: String) =
        Completable.create { emitter ->
            val user = hashMapOf(
                "name" to name,
                "phoneNo" to phoneNo,
                "photoURL" to photoURL
            )
            db.collection("users")
                .document(currentUser()!!.uid)
                .set(user)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful)
                            emitter.onComplete()
                        else
                            emitter.onError(it.exception!!)
                    }
                }
        }


    fun uploadFile(filePath: Uri, phoneNo: String) = Completable.create { emitter ->
        val riversRef: StorageReference = storageReference.child("images/$phoneNo.jpg")
        riversRef.putFile(filePath)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        riversRef.downloadUrl
                            .addOnCompleteListener { task ->
                                photoURL = task.result.toString()
                                emitter.onComplete()
                            }
                    } else
                        emitter.onError(it.exception!!)
                }
            }
    }
}