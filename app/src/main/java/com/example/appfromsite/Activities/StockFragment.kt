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
import com.example.appfromsite.Adapters.StockPhotoParser
import com.example.appfromsite.Adapters.StockRepository
import com.example.appfromsite.Adapters.WebParser
import com.example.appfromsite.R
import com.example.appfromsite.databinding.FragmentStockBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockFragment : Fragment() {
    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter
    private val repository by lazy {
        StockRepository(
            WebParser(),
            StockPhotoParser()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
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
            layoutManager = LinearLayoutManager(context)
            adapter = this@StockFragment.adapter
            context?.let { safeContext ->
                addItemDecoration(
                    DividerItemDecoration(
                        safeContext,
                        LinearLayoutManager.VERTICAL
                    )
                )
            }
        }
    }

    private fun loadStocks() {
        lifecycleScope.launch {
            try {
                val stocks = withContext(Dispatchers.IO) {
                    repository.getStocks()
                }

                if (!isAdded || context == null) return@launch

                if (stocks.isEmpty()) {
                    showToast("No stocks found")
                } else {
                    adapter.updateData(stocks)
                    binding.progressBar.visibility = View.GONE
                    binding.stockRecycler.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
                Log.e("StockFragment", "Error loading stocks", e)
            }
        }
    }

    private fun showToast(message: String?) {
        if (isAdded && context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}