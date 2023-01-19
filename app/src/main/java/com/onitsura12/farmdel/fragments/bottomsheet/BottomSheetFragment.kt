package com.onitsura12.farmdel.fragments.bottomsheet


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onitsura12.farmdel.databinding.FragmentBottomSheetBinding

import com.onitsura12.farmdel.models.ImageModel
import com.onitsura12.farmdel.recyclerView.GalleryAdapter
import com.onitsura12.farmdel.recyclerView.OnListItemClickListener


class BottomSheetFragment : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentBottomSheetBinding
    private val adapter = GalleryAdapter()
    private lateinit var viewModel: BottomSheetViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater)
        viewModel = BottomSheetViewModel(contentResolver = requireActivity().contentResolver)
        binding.rcView.layoutManager = GridLayoutManager(requireContext(), 2)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {

        binding.apply {
            //Setting up listener to adapter
            val listener = object : OnListItemClickListener {
                override fun onClick(view: ViewGroup, position: Int) {
                    val item = viewModel.imagesList.value!![position]
                    viewModel.markItem(item)
                    viewModel.selectedImage.value = item.path.toString()
                    selectImage()
                }
            }
            adapter.setListener(listener = listener)

            //Checking permissions
            permissionGranted()

            //Setting up RecyclerView
            viewModel.imagesList.observe(viewLifecycleOwner) {
                val images: List<ImageModel> = it
                adapter.submitList(images)
                rcView.adapter = adapter
            }
        }
    }


    private fun permissionGranted() {
        val activity = requireActivity()
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectImage(){
        val selectedImage = viewModel.selectedImage.value
        val bundle = Bundle()
        bundle.putString("selectedImage", selectedImage)
        setFragmentResult("selectedImage", bundle)
    }
}