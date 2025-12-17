package com.example.billbro.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "splits")
data class SplitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val expenseId: String,
    val userId: String,
    val balance: Double
){
    @Ignore
    constructor(
        expenseId: String,
        userId: String,
        balance: Double
    ) : this(0, expenseId, userId, balance)
}