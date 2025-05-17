package com.example.appfromsite.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.appfromsite.R
import com.example.appfromsite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.requestButton.setOnClickListener {
            goRequest()
        }

        prefs = getSharedPreferences("UserData", MODE_PRIVATE)

        setCurrentFragment(PhotoFragment())
        fragmentChanger()
    }

    private fun fragmentChanger() {
        binding.bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainItem -> {
                    binding.searchSmeta.visibility = View.GONE
                    setCurrentFragment(PhotoFragment())
                }
                R.id.workItem -> {
                    binding.searchSmeta.visibility = View.VISIBLE
                    setCurrentFragment(WorkFragment())
                }
                R.id.priceItem -> {
                    binding.searchSmeta.visibility = View.GONE
                    setCurrentFragment(StockFragment())
                }
            }
            true
        }
    }

    private fun goRequest() {
        startActivity(Intent(this, PaymentRequestActivity::class.java))
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }
}