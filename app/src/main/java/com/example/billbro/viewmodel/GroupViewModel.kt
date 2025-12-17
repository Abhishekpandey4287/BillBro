package com.example.billbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbro.data.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repo: GroupRepository
) : ViewModel() {

    fun getGroups(userId: String) = repo.getGroupsForUser(userId)

    fun addGroup(name: String, createdBy: String, members: List<String>) {
        viewModelScope.launch {
            repo.createGroup(name, createdBy, members)
        }
    }
}