package com.example.appfromsite.Activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appfromsite.Adapters.EmailSender
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

    private val recipientEmail = "isalce0451@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            if (isNetworkAvailable()) {
                createRequest()
            } else {
                Toast.makeText(this, "Нет интернет-соединения", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            cancelRequest()
        }
    }

    private fun createRequest() {
        val name = binding.requestName.text.toString().trim()
        val mail = binding.requestEmail.text.toString().trim()
        val theme = binding.requestTheme.text.toString().trim()
        val message = binding.requestMessage.text.toString().trim()

        if (name.isEmpty() || mail.isEmpty() || theme.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            return
        }

        val emailBody = """
            Новая заявка на оплату:

            Имя: $name
            Email: $mail
            Тема: $theme
            Сообщение:
            $message
        """.trimIndent()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val emailSender = EmailSender(mail)

                emailSender.sendEmail(
                    recipient = recipientEmail,
                    subject = "Заявка на оплату: $theme",
                    body = emailBody
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PaymentRequestActivity, "Заявка отправлена", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PaymentRequestActivity, MainActivity::class.java))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PaymentRequestActivity, "Ошибка отправки: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun cancelRequest() {
        finish()
    }
}