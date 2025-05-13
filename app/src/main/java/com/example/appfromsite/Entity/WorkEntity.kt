package com.example.appfromsite.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_table")
data class WorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val size: Double,
    val unit: String
)