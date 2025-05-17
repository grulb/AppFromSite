package com.example.appfromsite.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWorks(works: List<WorkEntity>)

    @Query("SELECT * FROM work_table ORDER BY name ASC")
    fun getAllWorks(): Flow<List<WorkEntity>>

    @Query("SELECT COUNT(*) FROM work_table")
    suspend fun getCount(): Int
}