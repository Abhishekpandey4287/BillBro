package com.example.billbro.data.dto
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.billbro.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)
    @Query("SELECT * FROM expenses WHERE groupId = :groupId")
    fun getExpenses(groupId: String): LiveData<List<ExpenseEntity>>
    // Add these missing methods
    @Query("SELECT * FROM expenses WHERE groupId = :groupId")
    fun getExpensesFlow(groupId: String): Flow<List<ExpenseEntity>>
    @Query("SELECT * FROM expenses WHERE expenseId = :expenseId")
    suspend fun getExpense(expenseId: String): ExpenseEntity?
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
    @Query("DELETE FROM expenses WHERE expenseId = :expenseId")
    suspend fun deleteExpense(expenseId: String)
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)
}