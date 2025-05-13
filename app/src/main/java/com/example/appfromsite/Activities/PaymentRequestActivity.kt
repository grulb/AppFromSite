package com.example.appfromsite.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appfromsite.DAO.PaymentRequestDAO
import com.example.appfromsite.DatabaseClient
import com.example.appfromsite.Entity.PaymentsRequestEntity
import com.example.appfromsite.R
import com.example.appfromsite.databinding.ActivityMainBinding
import com.example.appfromsite.databinding.ActivityPaymentRequestBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentRequestBinding
    private lateinit var database: DatabaseClient
    private lateinit var paymentRequestDAO: PaymentRequestDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = DatabaseClient.getDatabase(this)
        paymentRequestDAO = database.paymentRequestDAO()

        binding.createButton.setOnClickListener {
            createRequest()
        }
    }

    private fun createRequest() {
        val name = binding.requestName.text.toString()
        val mail = binding.requestEmail.text.toString()
        val theme = binding.requestTheme.text.toString()
        val message = binding.requestMessage.text.toString()

        if (name == "" || mail == "" || theme == "" || message == "") {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val userRequest = PaymentsRequestEntity(0, name, mail, theme, message)

            paymentRequestDAO.createRequest(userRequest)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@PaymentRequestActivity, "Ваша заявка успешно создана", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@PaymentRequestActivity, MainActivity::class.java))
            }
        }
    }
}