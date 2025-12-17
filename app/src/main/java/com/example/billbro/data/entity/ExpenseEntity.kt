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
    val date : Long = System.currentTimeMillis()
){
    @Ignore
    constructor(
        expenseId: String,
        groupId: String,
        amount: Double,
        paidBy: String,
        description: String
    ) : this(
        expenseId,
        groupId,
        amount,
        paidBy,
        description,
        System.currentTimeMillis()
    )
}