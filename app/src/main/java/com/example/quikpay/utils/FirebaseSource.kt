package com.example.quikpay.utils

import android.net.Uri
import android.util.Log
import com.example.quikpay.data.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FirebaseSource {
    private val TAG = FirebaseSource::class.java.simpleName
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
    var sentTransactions = mutableListOf<Transaction>()
    var pendingRequests = mutableListOf<PoolRequest>()
    var receivedTransactions = mutableListOf<Transaction>()
    private lateinit var photoURL: String

    fun currentUser() = auth.currentUser


    fun login(email: String, password: String) =
        Completable.create { emitter ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful)
                        emitter.onComplete()
                    else
                        emitter.onError(it.exception!!)
                }
            }
        }

    fun register(email: String, password: String) =
        Completable.create { emitter ->
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
            val issue = Complaint(message, currentUser()!!.email!!, date, timezone)
            Log.d(TAG, "created the issue item")
            val complaintsRef = db.collection("complaints")
            complaintsRef.add(issue)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            Log.d(TAG, "successfully submitted the issue")
                            emitter.onComplete()
                        } else {
                            Log.d(TAG, it.exception?.message!!)
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

    fun uploadFile(filePath: Uri, phoneNo: String) =
        Completable.create { emitter ->
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

    fun fetchUserDetails() =
        Completable.create { emitter ->
            val usersRef = db.collection("users")
            usersRef.document(currentUser()!!.uid).get()
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            Log.d(TAG, "Current user details: ${it.result?.data}")
                            userDetails = it.result?.toObject(User::class.java)!!
                        }
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
        }

    fun updateSentHistory() =
        Completable.create { emitter ->
            val transactionsRef =
                db.collection("transactions/" + currentUser()!!.uid + "/user_transactions")
            transactionsRef
                .whereEqualTo("type", "from")
                .get().addOnCompleteListener { snapshots ->
                    if (!emitter.isDisposed) {
                        if (snapshots.isSuccessful) {
                            Log.d(TAG, "Current sent data: ${snapshots.result?.size()}")
                            snapshots.result?.toObjects(Transaction::class.java)?.let {
                                sentTransactions.clear()
                                sentTransactions.addAll(it)
                            }
                        }
                        emitter.onComplete()
                    } else {
                        Log.d(TAG, "Current sent data: null")
                        emitter.onError(snapshots.exception!!)
                    }
                }
        }

    fun updateReceivedHistory() =
        Completable.create { emitter ->
            val transactionsRef =
                db.collection("transactions/" + currentUser()!!.uid + "/user_transactions")
            transactionsRef
                .whereEqualTo("type", "to")
                .get().addOnCompleteListener { snapshots ->
                    if (!emitter.isDisposed) {
                        if (snapshots.isSuccessful) {
                            Log.d(TAG, "Current received data: ${snapshots.result?.size()}")
                            snapshots.result?.toObjects(Transaction::class.java)?.let {
                                receivedTransactions.clear()
                                receivedTransactions.addAll(it)
                            }
                        }
                        emitter.onComplete()
                    } else {
                        Log.d(TAG, "Current received data: null")
                        emitter.onError(snapshots.exception!!)
                    }
                }
        }

    fun createRequests(participants: List<String>, target: Double) =
        Completable.create { emitter ->
            val amount = target / participants.size

            db.runBatch { batch ->
                for (participant in participants) {
                    val ref = db.collection("requests/").document()
                    val request =
                        PoolRequest(
                            userDetails.accountNo,
                            userDetails.name,
                            participant,
                            false,
                            amount
                        )
                    batch.set(ref, request)
                }
            }.addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
            }
        }

    fun createPool(description: String, participants: List<String>, target: Double) =
        Completable.create { emitter ->
            val poolsRef = db.collection("pools/")
            val pool = Pool(
                created_by = currentUser()!!.uid,
                description = description,
                participants = participants,
                target = target
            )
            poolsRef
                .add(pool)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

    fun fetchPendingRequests() = Completable.create { emitter ->
        val requestsRef = db.collection("requests")
        requestsRef.whereEqualTo("to_phoneNo", userDetails.phoneNo).whereEqualTo("paid", false)
            .get()
            .addOnCompleteListener { snapshots ->
                if (!emitter.isDisposed) {
                    if (snapshots.isSuccessful) {
                        snapshots.result?.toObjects(PoolRequest::class.java)?.let {
                            Log.d(TAG, "Successfully fetched pending requests: $it")
                            pendingRequests.clear()
                            pendingRequests.addAll(it)
                        }
                    }
                    emitter.onComplete()
                } else
                    emitter.onError(snapshots.exception!!)
            }
    }

}