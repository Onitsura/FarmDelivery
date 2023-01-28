package com.onitsura12.farmdel.fragments.shop

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAddShopItemBinding
import com.onitsura12.farmdel.fragments.bottomsheet.BottomSheetFragment
import com.onitsura12.farmdel.models.ImageModel
import com.onitsura12.farmdel.recyclerView.GalleryAdapter
import com.onitsura12.farmdel.recyclerView.SliderAdapter
import com.squareup.picasso.Picasso
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
                    viewModel.imageUrl.observe(viewLifecycleOwner){
                        val newList = viewModel.imagesList.value
                        Log.i("list", it)
                        if (!newList!!.contains(it)) {
                            newList.add(it)
                            viewModel.imagesList.value = newList
                        }
                    }
                }

            }

            saveButton.setOnClickListener {
                viewModel.addShopItem(viewModel.createShopItem(
                    title = etItemTitle.text.toString(),
                    cost = etItemCost.text.toString(),
                    imagePath = viewModel.imageUrl.value,
                    weight = etItemWeight.text.toString(),
                    deliveryDate = etItemDeliveryDate.text.toString(),
                    description = etItemDescription.text.toString(),
                    imagesArray = viewModel.imagesList.value!!
                    ), requireContext())

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



    private fun initRcView(){
        binding.apply {
            imagesRcView.layoutManager = LinearLayoutManager(requireContext(),
                RecyclerView.HORIZONTAL, false)
            imagesRcView.adapter = adapter

            viewModel.imagesList.observe(viewLifecycleOwner){
                adapter.submitList(viewModel.stringToImageModel(it))
            }
        }
    }


}