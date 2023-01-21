package com.onitsura12.farmdel.fragments.shop

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAddShopItemBinding
import com.onitsura12.farmdel.fragments.bottomsheet.BottomSheetFragment
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.squareup.picasso.Picasso
import java.io.*


class AddShopItemFragment : Fragment() {

    companion object {
        fun newInstance() = AddShopItemFragment()
    }

    private lateinit var viewModel: AddShopItemViewModel
    private lateinit var binding: FragmentAddShopItemBinding

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
                    Log.e("ivPath", imagePath)
                    viewModel.imagePath.value = imagePath
                }

            }

            saveButton.setOnClickListener {

                uploadImage(etItemTitle.text.toString())
                val newItem = ShopItem(
                    title = etItemTitle.text.toString(),
                    cost = etItemCost.text.toString(),
                    imagePath = viewModel.imageUrl.value,
                    count = "0",
                    weight = null
                )
                REF_DATABASE_ROOT.child(NODE_SUPPLIES).child(etItemTitle.text.toString())
                    .setValue(newItem)


            }
            viewModel.imagePath.observe(viewLifecycleOwner) {
                Picasso.get().load(it)
                    .placeholder(R.drawable.camera_icon)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .fit()
                    .into(ivPreview)
            }

        }

    }


    private fun uploadImage(title: String) {
        val storageRef = FirebaseStorage.getInstance().reference

        val titleRef = storageRef.child(title)


        val titleImagesRef = storageRef.child("images/${title}")
        val file = Uri.fromFile(
            File(
                createCopyAndReturnRealPath(
                    requireContext(), Uri.parse(viewModel.imagePath.value!!))!!))

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
                Log.e("ivPAth", viewModel.imageUrl.value.toString())
            } else {
                // Handle failures
                // ...
            }
        }
        viewModel.imageUrl.observe(viewLifecycleOwner){
            REF_DATABASE_ROOT.child(NODE_SUPPLIES).child(title).child("imagePath").setValue(it)
        }

    }





    private fun createCopyAndReturnRealPath(
        context: Context, uri: Uri
    ): String? {
        val contentResolver: ContentResolver = context.contentResolver ?: return null


        val filePath: String = context.applicationInfo.dataDir + File.separator + "temp_file"
        val file = File(filePath)
        try {
            val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }

}