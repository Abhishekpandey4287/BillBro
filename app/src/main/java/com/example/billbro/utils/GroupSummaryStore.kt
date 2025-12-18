//package com.example.billbro.utils
//
//import java.util.concurrent.ConcurrentHashMap
//
//object GroupSummaryStore {
//    private val summaries = ConcurrentHashMap<String, CharSequence>()
//
//    fun put(groupId: String, summary: CharSequence) {
//        summaries[groupId] = summary
//    }
//
//    fun get(groupId: String): CharSequence? {
//        return summaries[groupId]
//    }
//}