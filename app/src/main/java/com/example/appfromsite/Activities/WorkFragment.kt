package com.example.appfromsite.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfromsite.Adapters.WorkAdapters.CsvParser
import com.example.appfromsite.Adapters.WorkAdapters.WorkAdapter
import com.example.appfromsite.Adapters.WorkAdapters.WorkRepository
import com.example.appfromsite.Adapters.WorkAdapters.WorkViewModel
import com.example.appfromsite.Adapters.WorkAdapters.WorkViewModelFactory
import com.example.appfromsite.DatabaseClient
import com.example.appfromsite.Entity.WorkEntity
import com.example.appfromsite.R
import com.example.appfromsite.databinding.FragmentWorkBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkFragment : Fragment() {
    private lateinit var binding: FragmentWorkBinding
    private lateinit var adapter: WorkAdapter
    private lateinit var viewModel: WorkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        observeWorks()
        setupSearch()
        loadCsvFromRaw()
    }

    private fun setupViewModel() {
        val dao = DatabaseClient.getDatabase(requireContext()).workDAO()
        val repository = WorkRepository(dao)
        viewModel = ViewModelProvider(this, WorkViewModelFactory(repository))[WorkViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = WorkAdapter()
        binding.workRecycler.adapter = adapter
        binding.workRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeWorks() {
        lifecycleScope.launch {
            viewModel.searchResults.collect { works ->
                adapter.submitList(works)
            }
        }
    }

    private fun setupSearch() {
        binding.searchWork.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(s?.toString() ?: "")
                binding.cancelButton.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val clearButton = binding.root.findViewById<TextView>(R.id.cancelButton)
        clearButton.setOnClickListener {
            binding.searchWork.text.clear()
            binding.cancelButton.visibility = View.GONE
        }
    }

    private fun loadCsvFromRaw() {
        lifecycleScope.launch {
            if (viewModel.shouldLoadCsv()) {
                try {
                    val works = CsvParser.parseFromRaw(requireContext(), R.raw.remont_price)
                    if (works.isNotEmpty()) {
                        viewModel.insertAll(works)
                    } else {
                        Toast.makeText(requireContext(), "CSV пуст", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Ошибка чтения CSV", Toast.LENGTH_SHORT).show()
                    Log.e("WorkFragment", "Ошибка CSV", e)
                }
            }
        }
    }
}