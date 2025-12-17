package com.example.billbro.data.dto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.billbro.data.entity.GroupEntity

@Dao
interface GroupDao {

    @Insert
    suspend fun insertGroup(group: GroupEntity)

    @Query("SELECT * FROM `groups`")
    fun getGroups(): LiveData<List<GroupEntity>>

    @Query("""
    SELECT g.* FROM `groups` g
    INNER JOIN group_members gm
    ON g.groupId = gm.groupId
    WHERE gm.userId = :userId
""")
    fun getGroupsForUser(userId: String): LiveData<List<GroupEntity>>

    @Query("DELETE FROM `groups` WHERE groupId = :groupId")
    suspend fun deleteGroup(groupId: String)

    @Query("SELECT * FROM `groups` WHERE groupId = :groupId")
    suspend fun getGroup(groupId: String): GroupEntity?
}