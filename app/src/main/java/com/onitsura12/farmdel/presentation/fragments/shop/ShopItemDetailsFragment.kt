package com.onitsura12.farmdel.presentation.fragments.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.onitsura12.farmdel.databinding.FragmentShopItemDetailsBinding
import com.onitsura12.farmdel.presentation.recyclerView.SliderAdapter

class ShopItemDetailsFragment : Fragment() {


    private val viewModel: ShopItemDetailsViewModel by activityViewModels()
    private lateinit var binding: FragmentShopItemDetailsBinding
    private val adapter: SliderAdapter = SliderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopItemDetailsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getImages(requireArguments().getString("title")!!)


        binding.apply {
            viewModel.imagesList.observe(viewLifecycleOwner){
                adapter.setData(it, viewPagerImageSlider)
                viewPagerImageSlider.adapter = adapter
            }
            setupInfo()

            binding.backButton.setOnClickListener {
                findNavController().popBackStack()
            }

            viewPagerImageSlider.clipToPadding = false
            viewPagerImageSlider.clipChildren = false
            viewPagerImageSlider.offscreenPageLimit = 3
            viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val pageTransformer = CompositePageTransformer()
            pageTransformer.addTransformer(MarginPageTransformer(40))
            pageTransformer.addTransformer { page, position ->
            }
            viewPagerImageSlider.setPageTransformer(pageTransformer)


        }
    }


    private fun setupInfo(){
        viewModel.shopItem.observe(viewLifecycleOwner){
            binding.apply {
                title.text = viewModel.removeExtraWhitespaces(it!!.title)
                itemWeight.text = it.weight
                itemCost.text = it.cost
                tvDeliveryDate.text = it.deliveryDate
                description.text = it.description
            }
        }
    }




}