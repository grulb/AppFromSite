package com.example.appfromsite.DAO

import androidx.room.Dao
import androidx.room.Insert
import com.example.appfromsite.Activities.PaymentRequestActivity
import com.example.appfromsite.Entity.PaymentsRequestEntity

@Dao
interface PaymentRequestDAO {
    @Insert
    suspend fun createRequest(userRequest: PaymentsRequestEntity)
}