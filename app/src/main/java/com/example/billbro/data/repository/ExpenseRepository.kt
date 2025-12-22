package com.example.billbro.data.repository

import com.example.billbro.data.dto.ExpenseDao
import com.example.billbro.data.dto.GroupMemberDao
import com.example.billbro.data.dto.SplitDao
import com.example.billbro.data.entity.ExpenseEntity
import com.example.billbro.utils.SplitCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val splitDao: SplitDao,
    private val groupMemberDao: GroupMemberDao
) {

    suspend fun addExpenseWithSplit(
        expense: ExpenseEntity,
        splitType: SplitType = SplitType.EQUAL
    ) {
        val memberIds = groupMemberDao.getGroupMemberIds(expense.groupId)

        val splits = when (splitType) {
            SplitType.EQUAL ->
                SplitCalculator.calculateEqualSplits(
                    expenseId = expense.expenseId,
                    amount = expense.amount,
                    paidBy = expense.paidBy,
                    participants = memberIds
                )
            SplitType.PERCENTAGE -> emptyList()
            SplitType.EXACT -> emptyList()
            SplitType.SHARE -> emptyList()
        }

        expenseDao.insertExpense(expense)
        splitDao.insertSplits(splits)
    }

//    fun getExpensesOnce(groupId: String): Flow<List<ExpenseEntity>> {
//        return expenseDao.getExpensesFlow(groupId)
//    }

    fun getExpenses(groupId: String): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesFlow(groupId)
    }

    suspend fun deleteExpense(expenseId: String) {
        splitDao.deleteSplitsForExpense(expenseId)
        expenseDao.deleteExpense(expenseId)
    }

    /**
     * Result:
     *  - ("YOU_GET", +amount)
     *  - ("userId", -amount)
     *  - (null, 0.0)
     */
    fun getGroupNetBalance(
        groupId: String,
        currentUserId: String
    ): Flow<Pair<String?, Double>> = flow {

        val balances = splitDao.getBalancesForGroup(groupId)

        val me = balances.firstOrNull { it.userId == currentUserId }

        val result = when {
            me == null || me.total == 0.0 ->
                null to 0.0

            me.total > 0 ->
                "YOU_GET" to me.total

            else -> {
                val creditor = balances.firstOrNull { it.total > 0 }
                creditor?.userId to me.total
            }
        }

        emit(result)
    }
}

enum class SplitType {
    EQUAL, PERCENTAGE, EXACT, SHARE
}