package com.example.billbro.data.entity

data class GroupSummary(
    val groupId: String,
    val groupName: String?,
    val netBalance: Double
//    val total: Double,
//    val owes: List<Pair<String, Double>>,
//    val gets: List<Pair<String, Double>>
)
