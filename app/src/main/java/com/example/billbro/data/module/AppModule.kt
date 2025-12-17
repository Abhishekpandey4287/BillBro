package com.example.billbro.data.module

import android.content.Context
import com.example.billbro.data.database.AppDatabase
import com.example.billbro.data.dto.GroupMemberDao
import com.example.billbro.data.dto.UserDao
import com.example.billbro.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase
    {
        return AppDatabase.getDatabase(context)
    }
    @Singleton
    @Provides
    fun provideExpenseRepository(
        database: AppDatabase,
        groupMemberDao: GroupMemberDao
    ): ExpenseRepository {
        return ExpenseRepository(
            expenseDao = database.expenseDao(),
            splitDao = database.splitDao(),
        groupMemberDao = groupMemberDao
        )
    }

    @Singleton
    @Provides
    fun provideGroupRepository(
        database: AppDatabase,
        userDao: UserDao
    ): GroupRepository {
        return GroupRepository(
            groupDao = database.groupDao(),
            groupMemberDao = database.groupMemberDao(),
        userDao = userDao
        )
    }

    @Singleton
    @Provides
    fun provideBalanceRepository(database: AppDatabase): BalanceRepository {
        return BalanceRepository(database.splitDao())
    }
    @Singleton
    @Provides
    fun provideSettlementRepository(database: AppDatabase):
            SettlementRepository {
        return SettlementRepository(
            settlementDao = database.settlementDao(),
            splitDao = database.splitDao()
        )
    }
    @Singleton
    @Provides
    fun provideGroupMemberDao(database: AppDatabase): GroupMemberDao {
        return database.groupMemberDao()
    }
    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}