package com.example.billbro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.billbro.data.entity.ExpenseEntity
import com.example.billbro.data.entity.GroupSummaryUi
import com.example.billbro.data.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repo: GroupRepository
) : ViewModel() {

    fun getGroups(userId: String) = repo.getGroupsForUser(userId)

    fun addGroup(name: String, createdBy: String, members: List<String>) {
        viewModelScope.launch {
            repo.createGroup(name, createdBy, members)
        }
    }

    fun getGroupSummaries(userId: String): LiveData<List<GroupSummaryUi>> = liveData {

        // 1️⃣ Get all groups of user
//        val groups = repo.getGroupsOnce(userId) // suspend function
//
//        val summaries = groups.map { group ->
//
//            // 2️⃣ Get expenses for THIS group
//            val expenses = repo.getExpensesForGroup(group.groupId)
//
//            // 3️⃣ Calculate summary
//            val summaryText = calculateGroupSummary(expenses)
//
//            GroupSummaryUi(
//                groupId = group.groupId,
//                groupName = group.groupName,
//                summaryText = summaryText
//            )
//        }
//
//        emit(summaries)
    }

    private fun calculateGroupSummary(
        expenses: List<ExpenseEntity>
    ): CharSequence {

        if (expenses.isEmpty()) return "You are settled up"

        val users = expenses.map { it.paidBy }.distinct()
        val balanceMap = mutableMapOf<String, Double>()
        users.forEach { balanceMap[it] = 0.0 }

        expenses.forEach { expense ->
            val share = expense.amount / users.size

            balanceMap[expense.paidBy] =
                balanceMap.getValue(expense.paidBy) + (expense.amount - share)

            users.forEach { user ->
                if (user != expense.paidBy) {
                    balanceMap[user] =
                        balanceMap.getValue(user) - share
                }
            }
        }

        val entry = balanceMap.entries.firstOrNull { it.value != 0.0 }
            ?: return "You are settled up"

        val name = entry.key
        val amount = entry.value

        val raw = if (amount > 0) {
            "$name owes you ₹${"%.0f".format(amount)}"
        } else {
            "You owe $name ₹${"%.0f".format(-amount)}"
        }

        val spannable = android.text.SpannableString(raw)
        val start = raw.indexOf("₹")

        spannable.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            start,
            raw.length,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            android.text.style.ForegroundColorSpan(
                if (amount > 0)
                    android.graphics.Color.parseColor("#2E7D32")
                else
                    android.graphics.Color.parseColor("#C62828")
            ),
            start,
            raw.length,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannable
    }
}