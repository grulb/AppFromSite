package com.example.appfromsite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.appfromsite.DAO.PaymentRequestDAO
import com.example.appfromsite.DAO.WorkDAO
import com.example.appfromsite.Entity.PaymentsRequestEntity
import com.example.appfromsite.Entity.WorkEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [WorkEntity::class, PaymentsRequestEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseClient: RoomDatabase() {
    abstract fun workDAO(): WorkDAO
    abstract fun paymentRequestDAO(): PaymentRequestDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseClient? = null

        fun getDatabase(context: Context): DatabaseClient {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseClient::class.java,
                    "site_database"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = DatabaseClient.getDatabase(context)
            loadInitialWorks(database.workDAO())
        }
    }

    private suspend fun loadInitialWorks(workDao: WorkDAO) {
        val prices = parseCSV(context)
        workDao.insertAllWorks(prices)
    }

    private fun parseCSV(context: Context): List<WorkEntity> {
        val priceList = mutableListOf<WorkEntity>()
        try {
            context.assets.open("prices.csv").bufferedReader().use { reader ->
                reader.forEachLine { line ->
                    val parts = line.split(";")
                    if (parts.size == 3) {
                        priceList.add(WorkEntity(
                            name = parts[0],
                            size = parts[2].toDoubleOrNull() ?: 0.0,
                            unit = parts[1]
                        ))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return priceList
    }
}