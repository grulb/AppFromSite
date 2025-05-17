package com.example.appfromsite.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfromsite.Adapters.WorkAdapters.CSVParser
import com.example.appfromsite.Adapters.WorkAdapters.WorkAdapter
import com.example.appfromsite.Adapters.WorkAdapters.WorkRepository
import com.example.appfromsite.Adapters.WorkAdapters.WorkViewModel
import com.example.appfromsite.Adapters.WorkAdapters.WorkViewModelFactory
import com.example.appfromsite.DatabaseClient
import com.example.appfromsite.R
import com.example.appfromsite.databinding.FragmentWorkBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkFragment : Fragment() {
    private lateinit var binding: FragmentWorkBinding
    private lateinit var adapter: WorkAdapter
    private lateinit var viewModel: WorkViewModel
    private lateinit var csvParser: CSVParser

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
        loadInitialData()
    }

    private fun setupViewModel() {
        val dao = DatabaseClient.getDatabase(requireContext()).workDAO()
        val repository = WorkRepository(dao)
        viewModel = ViewModelProvider(
            this,
            WorkViewModelFactory(repository)
        )[WorkViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = WorkAdapter()
        binding.workRecycler.apply {
            adapter = this@WorkFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeWorks() {
        lifecycleScope.launch {
            viewModel.allWorks.collect { works ->
                adapter.submitList(works)
            }
        }
    }

    private fun loadInitialData() {
        lifecycleScope.launch {
            try {
                if (viewModel.shouldLoadData()) {
                    val works = withContext(Dispatchers.IO) {
                        csvParser.parseWorks(requireContext(), R.raw.remont_price)
                    }
                    viewModel.insertAll(works)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show()
            }
        }
    }
}