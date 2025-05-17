package com.example.appfromsite.Adapters.WorkAdapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WorkViewModel(private val repository: WorkRepository) : ViewModel() {
    val allWorks: Flow<List<WorkEntity>> = repository.allWorks

    suspend fun shouldLoadData(): Boolean = repository.isEmpty()

    fun insertAll(works: List<WorkEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAll(works)
    }
}