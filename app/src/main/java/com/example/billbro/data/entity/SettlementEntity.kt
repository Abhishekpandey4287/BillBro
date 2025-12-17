package com.example.billbro.data.entity
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
@Entity(tableName = "settlements")
data class SettlementEntity(
    @PrimaryKey val settlementId: String,
    val fromUser: String,
    val toUser: String,
    val amount: Double,
    val timestamp: Long
){
    @Ignore
    constructor(
        settlementId: String,
        fromUser: String,
        toUser: String,
        amount: Double
    ) : this(
        settlementId,
        fromUser,
        toUser,
        amount,
        System.currentTimeMillis()
    )
}