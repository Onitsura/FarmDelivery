package com.onitsura12.farmdel.fragments.shop

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentShopBinding
import com.onitsura12.farmdel.recyclerView.ShopAdapter

class ShopFragment : Fragment() {

    companion object {
        fun newInstance() = ShopFragment()
    }

    private lateinit var viewModel: ShopViewModel
    private lateinit var binding: FragmentShopBinding
    private val adapter: ShopAdapter = ShopAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ShopViewModel()
        binding = FragmentShopBinding.inflate(layoutInflater)

        parentFragmentManager.setFragmentResultListener(
            "newItem", viewLifecycleOwner
        ) { _, bundle ->
            val newItem =
                viewModel.convertToShopItem((bundle.getStringArrayList("newItem") as ArrayList<String>))
            viewModel.shopItemList.value!!.add(newItem)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addShopItem.visibility = View.VISIBLE
        binding.addShopItem.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_addShopItemFragment)
        }

        viewModel.shopItemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        initRcView()
    }


    private fun initRcView() {
        binding.shopRcView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.shopRcView.adapter = adapter
    }

}