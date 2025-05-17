package com.example.appfromsite.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appfromsite.Adapters.PhotoAdapter
import com.example.appfromsite.Adapters.PhotoRepository
import com.example.appfromsite.R
import com.example.appfromsite.databinding.FragmentPhotoBinding
import kotlinx.coroutines.launch

class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private val photoRepository = PhotoRepository()
    private val adapter = PhotoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadPhotos()
    }

    private fun setupRecyclerView() {
        binding.photoRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.photoRecycler.adapter = adapter
        binding.photoRecycler.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.black_stroke_box)!!)
            }
        )
    }

    private fun loadPhotos() {
        lifecycleScope.launch {
            try {
                val photos = photoRepository.parseSpecificPhotos()
                adapter.submitList(photos)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Ошибка загрузки фото: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}