//package com.example.billbro.data.module
//import android.content.Context
//import androidx.room.Room
//import com.example.billbro.data.database.AppDatabase
//import com.example.billbro.data.dto.*
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(
//        @ApplicationContext context: Context
//    ): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java,
//            "splitwise_db"
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//
//    @Provides fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()
//    @Provides fun provideSplitDao(db: AppDatabase): SplitDao = db.splitDao()
//    @Provides fun provideGroupDao(db: AppDatabase): GroupDao = db.groupDao()
//    @Provides fun provideGroupMemberDao(db: AppDatabase): GroupMemberDao = db.groupMemberDao()
//    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
//    @Provides fun provideSettlementDao(db: AppDatabase): SettlementDao = db.settlementDao()
//}