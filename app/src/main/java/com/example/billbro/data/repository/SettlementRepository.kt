package com.example.billbro.data.repository

import com.example.billbro.data.dto.SettlementDao
import com.example.billbro.data.dto.SplitDao
import com.example.billbro.utils.SettlementCalculator

class SettlementRepository(
    private val settlementDao: SettlementDao,
    private val splitDao: SplitDao
) {
    suspend fun settleUp(
        fromUser: String,
        toUser: String,
        amount: Double
    ) {
        val (settlement, splits) =
            SettlementCalculator.createSettlement(fromUser, toUser, amount)
        settlementDao.insertSettlement(settlement)
        splitDao.insertSplits(splits)
    }
}