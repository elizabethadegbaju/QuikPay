package com.example.quikpay.utils

import android.net.Uri
import android.util.Log
import com.example.quikpay.data.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable
import io.reactivex.Observable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FirebaseSource {
    private val TAG = FirebaseSource::class.java.simpleName
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val storage: StorageReference by lazy { FirebaseStorage.getInstance().reference }

    lateinit var userDetails: User
    var sentTransactions = mutableListOf<Transaction>()
    var pendingRequests = mutableListOf<PoolRequest>()
    var receivedTransactions = mutableListOf<Transaction>()
    var openPools = mutableListOf<Pool>()
    private lateinit var photoURL: String

    fun currentUser() = auth.currentUser

    fun login(email: String, password: String) =
        Completable.create { emitter ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        Log.d(TAG, "login: user $email logged in successfully")
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
        }

    fun register(email: String, password: String) =
        Completable.create { emitter ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        Log.d(TAG, "register: user $email registered successfully")
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
        }

    fun logout() = auth.signOut()

    fun saveUserDetails(name: String, phoneNo: String) =
        Completable.create { emitter ->
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("ddMMyy")
            val formatted = today.format(formatter)
            val accountNo = formatted + phoneNo.slice(6..9)
            val user = User(name, currentUser()!!.email!!, phoneNo, photoURL, accountNo)
            val usersRef = db.collection("users")
            usersRef.document(currentUser()!!.uid)
                .set(user)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            Log.d(TAG, "saveUserDetails: User details saved for $name")
                            emitter.onComplete()
                        } else
                            emitter.onError(it.exception!!)
                    }
                }
        }

    fun reportIssue(message: String, date: String, timezone: String) =
        Completable.create { emitter ->
            val issue = Complaint(message, currentUser()!!.email!!, date, timezone)
            Log.d(TAG, "reportIssue: created the issue item")
            val complaintsRef = db.collection("complaints")
            complaintsRef.add(issue)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            Log.d(TAG, "reportIssue: successfully submitted the issue")
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
            val imagesRef = storage.child("images/$phoneNo.jpg")
            imagesRef.putFile(filePath)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            imagesRef.downloadUrl
                                .addOnCompleteListener { task ->
                                    photoURL = task.result.toString()
                                    Log.d(TAG, "uploadFile: profile picture uploaded")
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
                            Log.d(TAG, "fetchUserDetails: Current user details: ${it.result?.data}")
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
                            Log.d(
                                TAG,
                                "updateSentHistory: Current sent data: ${snapshots.result?.size()}"
                            )
                            snapshots.result?.toObjects(Transaction::class.java)?.let {
                                sentTransactions.clear()
                                sentTransactions.addAll(it)
                            }
                        }
                        emitter.onComplete()
                    } else {
                        Log.d(TAG, "updateSentHistory: Current sent data: null")
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
                            Log.d(
                                TAG,
                                "updateReceivedHistory: Current received data: ${snapshots.result?.size()}"
                            )
                            snapshots.result?.toObjects(Transaction::class.java)?.let {
                                receivedTransactions.clear()
                                receivedTransactions.addAll(it)
                            }
                        }
                        emitter.onComplete()
                    } else {
                        Log.d(TAG, "updateReceivedHistory: Current received data: null")
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
                    Log.d(TAG, "createRequests: $participant added to batch")
                }
            }.addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        Log.d(
                            TAG,
                            "createRequests: All requests successfully created for these participants : $participants"
                        )
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
                            Log.d(TAG, "createPool: New pool created by ${currentUser()!!.uid}")
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

    fun fetchPendingRequests() =
        Completable.create { emitter ->
            val requestsRef = db.collection("requests")
            requestsRef
                .whereEqualTo("to_phoneNo", userDetails.phoneNo)
                .whereEqualTo("paid", false)
                .get()
                .addOnCompleteListener { snapshots ->
                    if (!emitter.isDisposed) {
                        if (snapshots.isSuccessful) {
                            snapshots.result?.toObjects(PoolRequest::class.java)?.let {
                                Log.d(
                                    TAG,
                                    "fetchPendingRequests: Successfully fetched pending requests: $it"
                                )
                                pendingRequests.clear()
                                pendingRequests.addAll(it)
                            }
                        }
                        emitter.onComplete()
                    } else
                        emitter.onError(snapshots.exception!!)
                }
        }

    fun fetchOpenPools() =
        Completable.create { emitter ->
            val poolsRef = db.collection("pools")
            poolsRef
                .whereEqualTo("created_by", currentUser()!!.uid)
                .get()
                .addOnCompleteListener { snapshots ->
                    if (!emitter.isDisposed) {
                        if (snapshots.isSuccessful) {
                            snapshots.result?.toObjects(Pool::class.java)?.let {
                                Log.d(TAG, "fetchOpenPools: Successfully fetched open pools: $it")
                                openPools.clear()
                                openPools.addAll(it)
                            }
                        }
                        emitter.onComplete()
                    } else {
                        emitter.onError(snapshots.exception!!)
                    }
                }
        }

    fun updateAccountBal(
        amount: Double,
        accountBal: Double = userDetails.accountBal,
        userUid: String = currentUser()!!.uid
    ) =
        Completable.create { emitter ->
            val userRef = db.collection("users").document(userUid)
            val newAccountBal = accountBal + amount
            userRef
                .update("accountBal", newAccountBal)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            Log.d(
                                TAG,
                                "updateAccountBal: Account balance updated to $newAccountBal for $userUid"
                            )
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

    fun findUser(accountNo: String) =
        Observable.create<QueryDocumentSnapshot> { emitter ->
            val usersRef = db.collection("users")
            usersRef
                .whereEqualTo("accountNo", accountNo)
                .get()
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            val document = it.result?.first()
                            val quikpayUser = document?.toObject(User::class.java)
                            Log.d(TAG, "findUser: $quikpayUser")
                            emitter.onNext(document!!)
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

    fun createTransaction(transaction: Transaction, userUid: String = currentUser()!!.uid) =
        Completable.create { emitter ->
            val userTransactionsReference =
                db.collection("transactions")
                    .document(userUid)
                    .collection("user_transactions")
            userTransactionsReference
                .add(transaction)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            Log.d(
                                TAG,
                                "createTransaction: Transaction created ${transaction.type} $userUid"
                            )
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                }
        }

}