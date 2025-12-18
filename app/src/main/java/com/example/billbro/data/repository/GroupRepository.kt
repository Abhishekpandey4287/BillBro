package com.example.billbro.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.billbro.data.dto.UserDao
import com.example.billbro.data.dto.GroupDao
import com.example.billbro.data.dto.GroupMemberDao
import com.example.billbro.data.entity.GroupEntity
import com.example.billbro.data.entity.GroupMemberEntity
import com.example.billbro.data.entity.UserEntity
import java.util.*
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao,
    private val groupMemberDao: GroupMemberDao,
    private val userDao: UserDao
) {

    suspend fun createGroup(
        groupName: String? = null,
        createdBy: String? = null,
        memberIds: List<String>,
        description: String? = null
    ): String {
        requireNotNull(createdBy) { "createdBy cannot be null" }

        val groupId = UUID.randomUUID().toString()
        Log.d("GROUP_CREATE", "New groupId = $groupId")

        userDao.insertUser(
            UserEntity(
                userId = createdBy,
                name = "You", // or fetch from Firebase profile
                balance = 0.0
            )
        )

        val group = GroupEntity(
            groupId = groupId,
            groupName = groupName,
            createdBy = createdBy,
            description = description,
            createdAt = System.currentTimeMillis(),
            currency = "INR"
        )

        groupDao.insertGroup(group)

        val allMembers = memberIds.distinct()

        val groupMembers = allMembers.map { userId ->
            GroupMemberEntity(groupId = groupId, userId = userId)
        }

        groupMemberDao.insertAllGroupMembers(groupMembers)

        return groupId
    }

    fun getGroups(): LiveData<List<GroupEntity>> {
        return groupDao.getGroups()
    }

    fun getGroupsForUser(userId: String): LiveData<List<GroupEntity>> {
        return groupDao.getGroupsForUser(userId)
    }

    suspend fun getGroupMembers(groupId: String): List<String> {
        return groupMemberDao.getGroupMemberIds(groupId)
    }

    suspend fun addMemberToGroup(groupId: String, userId: String) {
        groupMemberDao.insertGroupMember(
            GroupMemberEntity(groupId = groupId, userId = userId)
        )
    }

    suspend fun removeMemberFromGroup(groupId: String, userId: String) {
        groupMemberDao.removeMemberFromGroup(groupId, userId)
    }

    suspend fun deleteGroup(groupId: String) {
        groupMemberDao.deleteAllGroupMembers(groupId)
        groupDao.deleteGroup(groupId)
    }
}