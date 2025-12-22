package com.example.billbro.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val expenseId: String,
    val groupId: String,
    val amount: Double,
    val paidBy: String,
    val description: String,
    val date: Long = System.currentTimeMillis(),
    val splitBetween: List<String>
)