package com.example.billbro.data.dto

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.billbro.data.entity.BalanceResult
import com.example.billbro.data.entity.SplitEntity
import com.example.billbro.data.entity.UserBalance

@Dao
interface SplitDao {
    @Insert
    suspend fun insertSplit(split: SplitEntity)

    @Insert
    suspend fun insertSplits(splits: List<SplitEntity>)

    @Query("SELECT * FROM splits WHERE expenseId = :expenseId")
    suspend fun getSplitsForExpense(expenseId: String): List<SplitEntity>

    @Query("SELECT userId, SUM(balance) as total FROM splits GROUP BY userId")
    fun getNetBalances(): LiveData<List<UserBalance>>

    @Query("""
        SELECT s.userId, SUM(s.balance) as total 
        FROM splits s
        JOIN expenses e ON s.expenseId = e.expenseId
        WHERE e.groupId = :groupId
        GROUP BY s.userId
    """)
    suspend fun getBalancesForGroup(groupId: String): List<UserBalance>


    @Query("""
        SELECT userId AS userId,
               SUM(balance) AS total
        FROM splits
        GROUP BY userId
    """)
    fun getUserBalances(): List<UserBalance>

    @Query("""
        SELECT s.userId, SUM(s.balance) as total 
        FROM splits s
        JOIN expenses e ON s.expenseId = e.expenseId
        WHERE e.groupId = :groupId AND s.userId = :userId
    """)
    suspend fun getUserBalanceInGroup(groupId: String, userId: String): BalanceResult?

    @Query("DELETE FROM splits WHERE expenseId = :expenseId")
    suspend fun deleteSplitsForExpense(expenseId: String)
}