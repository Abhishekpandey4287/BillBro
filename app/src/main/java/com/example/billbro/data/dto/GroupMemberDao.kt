package com.example.billbro.data.dto

import androidx.room.*
import com.example.billbro.data.entity.GroupMemberEntity

@Dao
interface GroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupMember(groupMember: GroupMemberEntity)

    @Insert
    suspend fun insertAllGroupMembers(members: List<GroupMemberEntity>)

    @Insert suspend fun insertAll(members: List<GroupMemberEntity>)
    @Query("SELECT userId FROM group_members WHERE groupId =:groupId")
    suspend fun members(groupId: String): List<String>

    @Query("SELECT userId FROM group_members WHERE groupId = :groupId")
    suspend fun getGroupMemberIds(groupId: String): List<String>

    @Query("DELETE FROM group_members WHERE groupId = :groupId AND userId = :userId")
    suspend fun removeMemberFromGroup(groupId: String, userId: String)

    @Query("DELETE FROM group_members WHERE groupId = :groupId")
    suspend fun deleteAllGroupMembers(groupId: String)
}