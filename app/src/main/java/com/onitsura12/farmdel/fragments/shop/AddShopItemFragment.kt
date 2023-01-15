package com.onitsura12.farmdel.fragments.shop

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentAddShopItemBinding
import com.onitsura12.farmdel.fragments.bottomsheet.BottomSheetFragment
import com.squareup.picasso.Picasso

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
                    viewModel.imagePath.value = imagePath
                }

            }

         saveButton.setOnClickListener {
//             val bitmap: Bitmap? = (ivPreview.drawable as BitmapDrawable).bitmap
             val newItem = ShopItem(
                 title = etItemTitle.text.toString(),
                 cost = etItemCost.text.toString(),
                 imagePath = viewModel.imagePath.value,
                 count = "0",
                 weight = null
             )

             viewModel.shopItem.value = newItem
             Log.e("ShopItem", viewModel.shopItem.value.toString())

             val array = viewModel.convertToArrayList(newItem)
             val bundle = Bundle()
             bundle.putStringArrayList("newItem", array)
             setFragmentResult("newItem", bundle)

         }
            viewModel.imagePath.observe(viewLifecycleOwner){
                Picasso.get().load(it)
                    .placeholder(R.drawable.camera_icon)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .fit()
                    .into(ivPreview)
            }

        }

    }


}