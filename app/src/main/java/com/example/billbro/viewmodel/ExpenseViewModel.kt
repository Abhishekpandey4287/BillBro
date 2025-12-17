package com.example.billbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbro.data.entity.ExpenseEntity
import com.example.billbro.data.repository.ExpenseRepository
import com.example.billbro.data.repository.SplitType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expenses: StateFlow<List<ExpenseEntity>> = _expenses

    private val _balanceSummary =
        MutableStateFlow<Pair<String?, Double>?>(null)
    val balanceSummary: StateFlow<Pair<String?, Double>?> = _balanceSummary

    private var loadJob: Job? = null

    fun loadExpenses(groupId: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            expenseRepository.getExpenses(groupId)
                .collectLatest {
                    _expenses.value = it
                }
        }
    }

    fun loadBalance(groupId: String, currentUserId: String) {
        viewModelScope.launch {
            expenseRepository
                .getGroupNetBalance(groupId, currentUserId)
                .collect {
                    _balanceSummary.value = it
                }
        }
    }

    fun addExpense(
        groupId: String,
        amount: Double,
        paidBy: String,
        description: String,
        splitType: SplitType = SplitType.EQUAL
    ) {
        viewModelScope.launch {
            val expense = ExpenseEntity(
                expenseId = UUID.randomUUID().toString(),
                groupId = groupId,
                amount = amount,
                paidBy = paidBy,
                description = description,
                date = System.currentTimeMillis()
            )

            expenseRepository.addExpenseWithSplit(expense, splitType)
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            expenseRepository.deleteExpense(expenseId)
        }
    }
}