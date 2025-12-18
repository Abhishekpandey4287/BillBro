package com.example.billbro.data.entity

data class GroupWithBalance(
    val group: GroupEntity,
    val netBalance: Double,
    val owes: List<UserBalance>,
    val gets: List<UserBalance>
)