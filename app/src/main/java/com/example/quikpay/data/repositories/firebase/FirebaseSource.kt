package com.example.quikpay.data.repositories.firebase

import android.net.Uri
import android.util.Log
import com.example.quikpay.data.models.Complaint
import com.example.quikpay.data.models.Transaction
import com.example.quikpay.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private const val TAG = "FirebaseSource"

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

    lateinit var userDetails: User
    lateinit var sentTransactions: MutableList<Transaction>
    lateinit var receivedTransactions: MutableList<Transaction>
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
            val today = LocalDate.now(ZoneId.of("WAT"))
            val formatter = DateTimeFormatter.ofPattern("ddMMyy")
            val formatted = today.format(formatter)
            val accountNo = formatted + phoneNo[-4]
            val user = User(name, currentUser()!!.email!!, phoneNo, photoURL, accountNo)
            val usersRef = db.collection("users")
            usersRef.document(currentUser()!!.uid)
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

    fun reportIssue(message: String, date: String, timezone: String) =
        Completable.create { emitter ->
            Log.d("ReportIssueFirebase", "starting the process")
            val issue = Complaint(message, currentUser()!!.email!!, date, timezone)
            Log.d("ReportIssueFirebase", "created the issue item")
            val complaintsRef = db.collection("complaints")
            complaintsRef.add(issue)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            Log.d("ReportIssueFirebase", "successfully submitted the issue")
                            emitter.onComplete()
                        } else {
                            Log.d("ReportIssueFirebase", it.exception?.message!!)
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }


    fun uploadFile(filePath: Uri, phoneNo: String) = Completable.create { emitter ->
        val imagesRef = storageReference.child("images/$phoneNo.jpg")
        imagesRef.putFile(filePath)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        imagesRef.downloadUrl
                            .addOnCompleteListener { task ->
                                photoURL = task.result.toString()
                                emitter.onComplete()
                            }
                    } else
                        emitter.onError(it.exception!!)
                }
            }
    }

    fun fetchUserDetails() = Completable.create { emitter ->
        val usersRef = db.collection("users")
        usersRef.document(currentUser()!!.uid).get()
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        userDetails = it.result?.toObject(User::class.java)!!
                    }
                    emitter.onComplete()
                } else
                    emitter.onError(it.exception!!)
            }
    }

    fun updateSentHistory() = Completable.create { emitter ->
        val transactionsRef =
            db.collection("transactions/" + currentUser()!!.uid + "/user_transactions")
        transactionsRef
            .whereEqualTo("type", "from")
            .get().addOnCompleteListener { snapshots ->
                if (!emitter.isDisposed) {
                    if (snapshots.isSuccessful) {
                        Log.d(TAG, "Current sent data: ${snapshots.result?.size()}")
                        sentTransactions = snapshots.result!!.toObjects(Transaction::class.java)
                    }
                    emitter.onComplete()
                } else {
                    Log.d(TAG, "Current sent data: null")
                    emitter.onError(snapshots.exception!!)
                }
            }
    }

    fun updateReceivedHistory() = Completable.create { emitter ->
        val transactionsRef =
            db.collection("transactions/" + currentUser()!!.uid + "/user_transactions")
        transactionsRef
            .whereEqualTo("type", "to")
            .get().addOnCompleteListener { snapshots ->
                if (!emitter.isDisposed) {
                    if (snapshots.isSuccessful) {
                        Log.d(TAG, "Current received data: ${snapshots.result?.size()}")
                        receivedTransactions = snapshots.result!!.toObjects(Transaction::class.java)
                    }
                    emitter.onComplete()
                } else {
                    Log.d(TAG, "Current received data: null")
                    emitter.onError(snapshots.exception!!)
                }
            }
    }

}