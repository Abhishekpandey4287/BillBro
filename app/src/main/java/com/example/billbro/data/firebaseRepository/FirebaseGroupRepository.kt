package com.example.billbro.data.firebaseRepository

import com.example.billbro.data.entity.GroupEntity
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseGroupRepository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    fun createGroup(
        group: GroupEntity,
        memberIds: List<String>
    ) {
        val groupRef = db.collection("groups").document(group.groupId)

        // 1️⃣ Create group document
        groupRef.set(group)

        // 2️⃣ Create members subcollection
        memberIds.distinct().forEach { userId ->
            groupRef
                .collection("members")
                .document(userId)
                .set(
                    mapOf(
                        "userId" to userId,
                        "joinedAt" to System.currentTimeMillis()
                    )
                )
        }
    }

    fun listenGroupsForUser(
        userId: String,
        onUpdate: (List<GroupEntity>) -> Unit
    ) {
        db.collection("groups")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    onUpdate(emptyList())
                    return@addSnapshotListener
                }

                val result = mutableListOf<GroupEntity>()
                var pending = snapshot.size()

                snapshot.documents.forEach { groupDoc ->
                    groupDoc.reference
                        .collection("members")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { memberDoc ->
                            if (memberDoc.exists()) {
                                groupDoc.toObject(GroupEntity::class.java)
                                    ?.let { result.add(it) }
                            }
                            if (--pending == 0) onUpdate(result)
                        }
                }
            }
    }
}