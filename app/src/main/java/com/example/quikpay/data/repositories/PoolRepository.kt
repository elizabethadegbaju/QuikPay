package com.example.quikpay.data.repositories

import com.example.quikpay.data.repositories.firebase.FirebaseSource

class PoolRepository(private val firebase: FirebaseSource) {
    fun createPool(description: String, participants: List<String> = listOf(), target: Double) =
        firebase.createPool(description, participants, target)
}