package com.example.appfromsite.Adapters

import com.example.appfromsite.DAO.WorkDAO
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.flow.Flow

class WorkRepository(private val workDAO: WorkDAO) {
    val allPrices: Flow<List<WorkEntity>> = workDAO.getAllWorks()

    suspend fun insertAll(prices: List<WorkEntity>) {
        workDAO.insertAllWorks(prices)
    }
}