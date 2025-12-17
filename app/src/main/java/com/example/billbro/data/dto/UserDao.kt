package com.example.billbro.data.dto
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.billbro.data.entity.UserEntity
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUser(userId: String): UserEntity?
    @Query("SELECT * FROM users WHERE userId IN (:userIds)")
    suspend fun getUsersByIds(userIds: List<String>): List<UserEntity>
    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<UserEntity>>
}