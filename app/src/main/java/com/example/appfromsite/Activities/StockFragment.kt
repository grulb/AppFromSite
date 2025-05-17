package com.example.appfromsite.Activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfromsite.Adapters.StockAdapter
import com.example.appfromsite.Adapters.StockRepository
import com.example.appfromsite.Adapters.WebParser
import com.example.appfromsite.R
import com.example.appfromsite.databinding.FragmentStockBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockFragment : Fragment() {
    private lateinit var binding: FragmentStockBinding
    private lateinit var adapter: StockAdapter
    private val repository by lazy { StockRepository(WebParser()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStockBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadStocks()
    }

    private fun setupRecyclerView() {
        adapter = StockAdapter()
        binding.stockRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StockFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun loadStocks() {
        lifecycleScope.launch {
            try {
                val stocks = withContext(Dispatchers.IO) {
                    repository.getStocks()
                }

                if (stocks.isEmpty()) {
                    Toast.makeText(requireContext(), "No stocks found", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.updateData(stocks)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("StockFragment", "Error loading stocks", e)
            }
        }
    }
}