package com.example.billbro.data.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
@Entity(
    tableName = "group_members",
    primaryKeys = ["groupId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId"), Index("userId")]
)
data class GroupMemberEntity(
    val groupId: String,
    val userId: String,
    val joinedAt: Long = System.currentTimeMillis()
){
    @Ignore
    constructor(
        groupId: String,
        userId: String
    ) : this(
        groupId,
        userId,
        System.currentTimeMillis()
    )
}