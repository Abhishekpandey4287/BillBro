package com.example.billbro.utils

import com.example.billbro.data.entity.SettlementEntity
import com.example.billbro.data.entity.SplitEntity
import java.util.UUID

object SettlementCalculator {
    fun createSettlement(
        fromUser: String,
        toUser: String,
        amount: Double
    ): Pair<SettlementEntity, List<SplitEntity>> {
        val settlement = SettlementEntity(

            UUID.randomUUID().toString(),
            fromUser,
            toUser,
            amount,
            System.currentTimeMillis()
        )
        val splits = listOf(
            SplitEntity(
                expenseId = settlement.settlementId,
                userId = fromUser,
                balance = amount
            ),
            SplitEntity(
                expenseId = settlement.settlementId,
                userId = toUser,
                balance = -amount
            )
        )
        return settlement to splits
    }
}