package com.example.billbro.utils

import com.example.billbro.data.entity.UserBalance
import kotlin.math.abs

object BalanceCalculator {

    fun calculateSettlements(balances: List<UserBalance>): List<Triple<String, String, Double>> {
        val settlements = mutableListOf<Triple<String, String, Double>>()
        val debtors = mutableListOf<Pair<String, Double>>()
        val creditors = mutableListOf<Pair<String, Double>>()

        // Separate debtors and creditors
        balances.forEach { balance ->
            if (balance.total < 0) {
                debtors.add(balance.userId to abs(balance.total))
            } else if (balance.total > 0) {
                creditors.add(balance.userId to balance.total)
            }
        }

        var i = 0
        var j = 0

        while (i < debtors.size && j < creditors.size) {
            val (debtor, debtAmount) = debtors[i]
            val (creditor, creditAmount) = creditors[j]

            val settleAmount = minOf(debtAmount, creditAmount)
            settlements.add(Triple(debtor, creditor, settleAmount))

            if (debtAmount - settleAmount == 0.0) {
                i++
            } else {
                debtors[i] = debtor to (debtAmount - settleAmount)
            }

            if (creditAmount - settleAmount == 0.0) {
                j++
            } else {
                creditors[j] = creditor to (creditAmount - settleAmount)
            }
        }

        return settlements
    }

    fun getSimplifiedBalances(balances: List<UserBalance>): List<Pair<String, Double>> {
        return balances.map { it.userId to it.total }
    }
}