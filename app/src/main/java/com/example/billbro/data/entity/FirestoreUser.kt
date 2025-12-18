package com.example.billbro.data.entity

// User collection structure
data class FirestoreUser(
    val userId: String,
    val email: String,
    val name: String,
    val balance: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)


