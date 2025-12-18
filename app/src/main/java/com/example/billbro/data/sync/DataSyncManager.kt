//package com.example.billbro.data.sync
//
//import com.example.billbro.data.firebaseRepository.FirebaseRepository
//import com.example.billbro.data.repository.ExpenseRepository
//import jakarta.inject.Inject
//import jakarta.inject.Singleton
//
//@Singleton
//class DataSyncManager @Inject constructor(
//    private val firebaseRepo: FirebaseRepository,
//    private val localRepo: ExpenseRepository
//) {
//    suspend fun syncGroupData(groupId: String) {
//        val firebaseExpenses = firebaseRepo.getExpensesForGroup(groupId)
//
//        firebaseExpenses.forEach { expense ->
//            localRepo.addExpenseWithSplit(expense)
//        }
//    }
//
//    suspend fun syncUserData(userId: String) {
//        // Sync user groups and balances
//    }
//}