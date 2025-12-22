package com.example.billbro.data.entity

data class GroupSummary(
    val groupId: String,
    val groupName: String,
    val summaryText: String,
    val totalBalance: Double = 0.0
)
