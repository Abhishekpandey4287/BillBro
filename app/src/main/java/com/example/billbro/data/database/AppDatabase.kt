package com.example.billbro.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.billbro.data.dto.ExpenseDao
import com.example.billbro.data.dto.GroupDao
import com.example.billbro.data.dto.GroupMemberDao
import com.example.billbro.data.dto.SettlementDao
import com.example.billbro.data.dto.SplitDao
import com.example.billbro.data.dto.UserDao
import com.example.billbro.data.entity.*
@Database(
    entities = [
        UserEntity::class,
        GroupEntity::class,
        ExpenseEntity::class,
        SplitEntity::class,
        SettlementEntity::class,
        GroupMemberEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun splitDao(): SplitDao
    abstract fun groupDao(): GroupDao
    abstract fun settlementDao(): SettlementDao
    abstract fun userDao(): UserDao
    abstract fun groupMemberDao(): GroupMemberDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "splitwise_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}