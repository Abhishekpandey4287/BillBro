package com.example.billbro.data.firebaseRepository

import com.example.billbro.data.entity.ExpenseEntity
import com.example.billbro.data.entity.GroupEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun syncGroup(group: GroupEntity) {
        db.collection("groups")
            .document(group.groupId)
            .set(group)
            .await()
    }

    suspend fun syncExpense(expense: ExpenseEntity) {
        db.collection("expenses")
            .document(expense.expenseId)
            .set(expense)
            .await()
    }

    suspend fun getGroupsForUser(userId: String): List<GroupEntity> {
        val groupsSnapshot = db.collection("groups").get().await()
        val result = mutableListOf<GroupEntity>()

        for (doc in groupsSnapshot.documents) {
            val memberDoc = doc.reference
                .collection("members")
                .document(userId)
                .get()
                .await()

            if (memberDoc.exists()) {
                doc.toObject(GroupEntity::class.java)
                    ?.let { result.add(it) }
            }
        }
        return result
    }
}