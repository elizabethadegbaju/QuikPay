package com.example.quikpay.data.repositories

import com.example.quikpay.data.models.PoolRequest
import com.example.quikpay.utils.FirebaseSource

class PoolRepository(private val firebase: FirebaseSource) {
    fun createPool(description: String, participants: List<String>, target: Double) =
        firebase.createPool(description, participants, target)

    fun createRequests(participants: List<String>, target: Double) =
        firebase.createRequests(participants, target)

    fun pendingRequests() = firebase.pendingRequests

    fun fetchPendingRequests() = firebase.fetchPendingRequests()

    fun currentUserDetails() = firebase.userDetails

    fun fetchOpenPools() = firebase.fetchOpenPools()

    fun openPools() = firebase.openPools

    fun fetchUserDetails() = firebase.fetchUserDetails()

    fun deleteRequest(request: PoolRequest) = firebase.deleteRequest(request)
}