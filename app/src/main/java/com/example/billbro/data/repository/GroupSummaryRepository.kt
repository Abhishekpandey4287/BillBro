package com.example.billbro.data.repository

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class GroupSummaryRepository @Inject constructor() {

    private val summaries =
        MutableStateFlow<Map<String, CharSequence>>(emptyMap())

    val summaryFlow = summaries.asStateFlow()

    fun update(groupId: String, summary: CharSequence) {
        summaries.value += (groupId to summary)
    }
}
