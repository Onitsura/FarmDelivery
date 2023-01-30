package com.onitsura12.farmdel.fragments.shop

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.onitsura12.domain.models.AdditionalServices
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAddShopItemBinding
import com.onitsura12.farmdel.fragments.bottomsheet.BottomSheetFragment
import com.onitsura12.farmdel.recyclerView.GalleryAdapter
import java.io.File


class AddShopItemFragment : Fragment() {


    private lateinit var viewModel: AddShopItemViewModel
    private lateinit var binding: FragmentAddShopItemBinding
    private val adapter: GalleryAdapter = GalleryAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = AddShopItemViewModel()
        binding = FragmentAddShopItemBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()




        binding.apply {

            backButton.setOnClickListener {
                findNavController().popBackStack(R.id.shopFragment, inclusive = false)
            }

            ivPreview.setOnClickListener {
                val bottomSheet = BottomSheetFragment()
                bottomSheet.show(parentFragmentManager, "Gallery")
                parentFragmentManager.setFragmentResultListener(
                    "selectedImage", viewLifecycleOwner
                ) { _, bundle ->
                    val imagePath = (bundle.getString
                        ("selectedImage")!!)
                    viewModel.imagePath.value = imagePath

                    val title: String = viewModel.getRandomName().toString()
                    uploadImage(title)
                    viewModel.imageUrl.observe(viewLifecycleOwner) {
                        val newList = viewModel.imagesList.value
                        if (!newList!!.contains(it)) {
                            newList.add(it)
                            viewModel.imagesList.value = newList
                        }
                    }
                }

            }

            saveButton.setOnClickListener {
                if (!isInputEmpty()) {
                    viewModel.addShopItem(
                        viewModel.createShopItem(
                            title = etItemTitle.text.toString(),
                            cost = etItemCost.text.toString(),
                            imagePath = viewModel.imageUrl.value,
                            weight = etItemWeight.text.toString(),
                            deliveryDate = etItemDeliveryDate.text.toString(),
                            description = etItemDescription.text.toString(),
                            imagesArray = viewModel.imagesList.value!!,
                            additionalServices = addAdditionalServices()
                        ), requireContext()
                    )
                }
            }

        }

    }


    private fun uploadImage(title: String) {
        val storageRef = FirebaseStorage.getInstance().reference

        val titleRef = storageRef.child(title)


        val titleImagesRef = storageRef.child("images/${title}")
        val file = Uri.fromFile(
            File(
                viewModel.createCopyAndReturnRealPath(
                    requireContext(),
                    Uri.parse(viewModel.imagePath.value!!)
                )!!
            )
        )

        val uploadTask = titleImagesRef.putFile(file)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            titleImagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val downloadUri = task.result
                viewModel.imageUrl.value = downloadUri.toString()
                Toast.makeText(context, "Фото добавлено", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun initRcView() {
        binding.apply {
            imagesRcView.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL, false
            )
            imagesRcView.adapter = adapter

            viewModel.imagesList.observe(viewLifecycleOwner) {
                adapter.submitList(viewModel.stringToImageModel(it))
            }
        }
    }

    private fun addAdditionalServices(): AdditionalServices? {
        var additionalServices: AdditionalServices? = null
        if (binding.checkboxAdditional.isChecked) {
            additionalServices = viewModel.createAdditional(
                title = binding.etAdditionalTitle.text.toString(),
                price = binding.etAdditionalPrice.text.toString()
            )
        }
        return additionalServices
    }

    private fun isInputEmpty(): Boolean {
        if (isEmptyCost() || isEmptyTitle() || isEmptyWeight()) {
            Toast.makeText(
                requireContext(), "Должны быть заполнены: Название, вес, стоимость " +
                        "и дата доставки товара", Toast
                    .LENGTH_SHORT
            ).show()
        }

        return isEmptyCost() || isEmptyTitle() || isEmptyWeight() || isEmptyDate()
    }

    private fun isEmptyTitle(): Boolean {
        return binding.etItemTitle.text.isBlank() || binding.etItemTitle.text.isEmpty()


    }

    private fun isEmptyCost(): Boolean {
        return binding.etItemCost.text.isBlank() || binding.etItemCost.text.isEmpty()
    }

    private fun isEmptyWeight(): Boolean {
        return binding.etItemWeight.text.isBlank() || binding.etItemWeight.text.isEmpty()
    }

    private fun isEmptyDate(): Boolean {
        return binding.etItemDeliveryDate.text.isBlank() || binding.etItemDeliveryDate.text
            .isEmpty()
    }


}