package com.example.billbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbro.data.repository.BalanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val balanceRepository: BalanceRepository
) : ViewModel() {

    private val _balances = MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val balances: StateFlow<List<Pair<String, Double>>> = _balances

    private val _settlements = MutableStateFlow<List<Triple<String, String, Double>>>(emptyList())
    val settlements: StateFlow<List<Triple<String, String, Double>>> = _settlements

    fun loadGroupBalances(groupId: String) {
        viewModelScope.launch {
            val balances = balanceRepository.getGroupBalances(groupId)
            _balances.value = balances
            _settlements.value = balanceRepository.calculateSettlements(balances)
        }
    }

    fun loadOverallBalances(userId: String) {
        viewModelScope.launch {
            val balances = balanceRepository.getOverallBalances(userId)
            _balances.value = balances
        }
    }
}