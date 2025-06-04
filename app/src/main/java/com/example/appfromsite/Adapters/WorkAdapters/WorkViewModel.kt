package com.example.appfromsite.Adapters.WorkAdapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkViewModel(private val repository: WorkRepository) : ViewModel() {
    val searchResults = MutableStateFlow<List<WorkEntity>>(emptyList())

    private var allWorks = emptyList<WorkEntity>()
    private var currentQuery = ""

    init {
        viewModelScope.launch {
            repository.allWorks.collect { works ->
                allWorks = works
                applyFilter()
            }
        }
    }

    fun search(query: String) {
        currentQuery = query
        applyFilter()
    }

    private fun applyFilter() {
        val filtered = if (currentQuery.isBlank()) {
            allWorks
        } else {
            allWorks.filter { it.name.contains(currentQuery, ignoreCase = true) }
        }
        searchResults.value = filtered
    }

    fun insertAll(works: List<WorkEntity>) = viewModelScope.launch {
        repository.insertAll(works)
    }

    suspend fun shouldLoadCsv(): Boolean = repository.shouldLoadFromCsv()
}