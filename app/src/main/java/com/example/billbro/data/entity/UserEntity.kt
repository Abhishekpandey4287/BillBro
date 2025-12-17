package com.example.billbro.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val balance: Double
)
{
    @Ignore
    constructor(userId: String, name: String) : this(userId, name, 0.0)
}
//@Entity(tableName = "users")
//data class UserEntity(
//    @PrimaryKey val userId: String,
//    val name: String,
//    val email: String,
//    val phone: String? = null,
//    val profileImage: String? = null,
//    val balance: Double = 0.0
//)
//{
//    @Ignore
//    constructor(userId: String, name: String) : this(userId, name)
//}
