package com.example.appfromsite.Adapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WorkViewModel(private val repository: WorkRepository): ViewModel() {
    val allPrices: Flow<List<WorkEntity>> = repository.allPrices

    fun insertAll(prices: List<WorkEntity>) = viewModelScope.launch {
        repository.insertAll(prices)
    }
}