package com.example.billbro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val groupId: String,
    val groupName: String?,
    val createdBy: String?,
    val createdAt: Long,
    val currency: String,
    val description: String?
)