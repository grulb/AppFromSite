package com.example.appfromsite.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_requests_table")
data class PaymentsRequestEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    val userName: String,
    val email: String,
    val theme: String,
    val message: String
)