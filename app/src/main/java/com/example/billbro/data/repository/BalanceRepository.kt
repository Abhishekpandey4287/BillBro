package com.example.billbro.data.repository

import com.example.billbro.data.dto.SplitDao
import com.example.billbro.utils.BalanceCalculator
import javax.inject.Inject

class BalanceRepository @Inject constructor(
    private val splitDao: SplitDao
) {
    suspend fun getGroupBalances(groupId: String): List<Pair<String, Double>> {
        val balances = splitDao.getBalancesForGroup(groupId)
        return BalanceCalculator.getSimplifiedBalances(balances)
    }

    suspend fun calculateSettlements(balances: List<Pair<String, Double>>): List<Triple<String, String, Double>> {
        val userBalances = balances.map {
            com.example.billbro.data.entity.UserBalance(it.first, it.second)
        }
        return BalanceCalculator.calculateSettlements(userBalances)
    }

    suspend fun getOverallBalances(userId: String): List<Pair<String, Double>> {
        // TODO: Implement based on your logic
        return emptyList()
    }
}