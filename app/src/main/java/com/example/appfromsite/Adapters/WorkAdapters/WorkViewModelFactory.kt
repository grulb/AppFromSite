package com.example.appfromsite.Adapters.WorkAdapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkViewModelFactory(private val repository: WorkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}