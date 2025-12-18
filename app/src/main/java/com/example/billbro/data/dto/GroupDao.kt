package com.example.billbro.data.dto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.billbro.data.entity.GroupEntity

@Dao
interface GroupDao {

//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    suspend fun insertGroup(group: GroupEntity)

    @Query("SELECT * FROM `groups`")
    fun getGroups(): LiveData<List<GroupEntity>>

    @Transaction
    @Query("""
        SELECT DISTINCT g.* FROM `groups` g
        INNER JOIN group_members gm
        ON g.groupId = gm.groupId
        WHERE gm.userId = :userId
        ORDER BY g.createdAt DESC
    """)
    fun getGroupsForUser(userId: String): LiveData<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertGroup(group: GroupEntity)

    @Query("DELETE FROM `groups` WHERE groupId = :groupId")
    suspend fun deleteGroup(groupId: String)

    @Query("SELECT * FROM `groups` WHERE groupId = :groupId")
    suspend fun getGroup(groupId: String): GroupEntity?
}