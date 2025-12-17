package com.example.billbro.data.firebaseRepository
import com.example.billbro.data.entity.ExpenseEntity
import com.example.billbro.utils.SplitCalculator
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class FirebaseExpenseRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    fun addExpense(
        expense: ExpenseEntity,
        participants: List<String>
    ) {
        db.collection("expenses")
            .document(expense.expenseId)
            .set(expense)
        val splits = SplitCalculator.calculateEqualSplits(
            expense.expenseId,
            expense.amount,
            expense.paidBy,
            participants
        )
        splits.forEach { split ->
            db.collection("splits")
                .document()
                .set(split)
        }
    }
    fun listenUserBalance(
        userId: String,
        onUpdate: (Double) -> Unit
    ) {
        db.collection("splits")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _
                ->
                val total = snapshot?.documents
                    ?.sumOf { it.getDouble("balance") ?: 0.0 }
                    ?: 0.0
                onUpdate(total)
            }
    }
}