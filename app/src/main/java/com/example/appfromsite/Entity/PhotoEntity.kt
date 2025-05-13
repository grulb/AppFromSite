package com.example.appfromsite.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val url: String,
    val title: String
)