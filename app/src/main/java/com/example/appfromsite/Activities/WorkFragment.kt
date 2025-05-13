package com.example.appfromsite.Activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfromsite.Adapters.WorkAdapter
import com.example.appfromsite.Entity.WorkEntity
import com.example.appfromsite.R
import com.example.appfromsite.databinding.FragmentWorkBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class WorkFragment : Fragment() {
    private lateinit var binding: FragmentWorkBinding
    private lateinit var adapter: WorkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WorkAdapter(emptyList())
        binding.workRecycler.adapter = adapter
        binding.workRecycler.layoutManager = LinearLayoutManager(requireContext())

        loadDataFromCSV()
    }

    private fun loadDataFromCSV() {
        lifecycleScope.launch {
            val prices = readCSVData()
            adapter = WorkAdapter(prices)
            binding.workRecycler.adapter = adapter
        }
    }

    private suspend fun readCSVData(): List<WorkEntity> = withContext(Dispatchers.IO) {
        val prices = mutableListOf<WorkEntity>()
        try {
            val inputStream = resources.openRawResource(R.raw.remont_price)
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.forEachIndexed { index, line ->
                    try {
                        val processedLine = if (line.contains("\"")) {
                            line.replace("\"\"", "\"")
                        } else {
                            line
                        }

                        val parts = processedLine.split(";")
                        if (parts.size == 3) {
                            prices.add(
                                WorkEntity(
                                id = index, // Используем индекс как временный ID
                                name = parts[0].trim(),
                                size = parts[2].trim().toDoubleOrNull() ?: 0.0,
                                unit = parts[1].trim()
                            )
                            )
                        } else {
                            Log.w("CSVParser", "Invalid line format: $line")
                        }
                    } catch (e: Exception) {
                        Log.e("CSVParser", "Error parsing line: $line", e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PriceFragment", "Error reading CSV", e)
        }
        return@withContext prices
    }
}