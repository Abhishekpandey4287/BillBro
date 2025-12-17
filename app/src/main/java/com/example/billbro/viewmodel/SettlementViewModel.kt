package com.example.billbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbro.data.repository.SettlementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettlementViewModel @Inject constructor(
    private val repo: SettlementRepository
) : ViewModel() {
    fun settle(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            repo.settleUp(from, to, amount)
        }
    }
}