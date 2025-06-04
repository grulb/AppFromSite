package com.example.appfromsite.Adapters.WorkAdapters

import com.example.appfromsite.DAO.WorkDAO
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.flow.Flow

class WorkRepository(private val workDAO: WorkDAO) {
    val allWorks: Flow<List<WorkEntity>> = workDAO.getAllWorks()

    suspend fun shouldLoadFromCsv(): Boolean = workDAO.getCount() == 0

    suspend fun insertAll(works: List<WorkEntity>) {
        workDAO.insertAllWorks(works)
    }
}