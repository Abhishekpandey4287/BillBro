package com.example.billbro.utils

import com.example.billbro.data.entity.SplitEntity
import kotlin.math.roundToInt

object SplitCalculator {

    fun equalSplit(
        expenseId: String,
        amount: Double,
        paidBy: String,
        users: List<String>
    ): List<SplitEntity> {
        val share = amount / users.size
        return users.map {
            SplitEntity(
                expenseId = expenseId,
                userId = it,
                balance = if (it == paidBy) amount - share else -share
            )
        }
    }

    fun calculateEqualSplits(
        expenseId: String,
        amount: Double,
        paidBy: String,
        participants: List<String>
    ): List<SplitEntity> {
        if (participants.isEmpty()) return emptyList()

        val share = (amount * 100 / participants.size).roundToInt() / 100.0
        var totalDistributed = 0.0

        val splits = mutableListOf<SplitEntity>()

        participants.forEachIndexed { index, userId ->
            val userShare = if (index == participants.lastIndex) {
                // Last person gets the remaining amount to avoid floating point issues
                amount - totalDistributed
            } else {
                share
            }

            val balance = if (userId == paidBy) {
                amount - userShare  // Positive balance for payer
            } else {
                -userShare  // Negative balance for others
            }

            splits.add(
                SplitEntity(
                    expenseId = expenseId,
                    userId = userId,
                    balance = balance
                )
            )

            totalDistributed += userShare
        }

        return splits
    }

    fun calculatePercentageSplits(
        expenseId: String,
        amount: Double,
        paidBy: String,
        participants: Map<String, Double> // userId to percentage
    ): List<SplitEntity> {
        val splits = mutableListOf<SplitEntity>()

        participants.forEach { (userId, percentage) ->
            val share = (amount * percentage / 100.0)
            val balance = if (userId == paidBy) {
                amount - share
            } else {
                -share
            }

            splits.add(
                SplitEntity(
                    expenseId = expenseId,
                    userId = userId,
                    balance = balance
                )
            )
        }

        return splits
    }
}