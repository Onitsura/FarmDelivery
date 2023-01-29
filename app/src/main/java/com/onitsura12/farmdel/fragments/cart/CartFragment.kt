package com.onitsura12.farmdel.fragments.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentCartBinding
import com.onitsura12.farmdel.recyclerView.CartAdapter

class CartFragment : Fragment() {


    private lateinit var viewModel: CartViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        viewModel = CartViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_confirmOrderFragment)
        }

        viewModel.totalCost.observe(viewLifecycleOwner){
            binding.tvCartTotalCost.text = it.toString()
        }

        initRcView()
    }



    private fun initRcView() {
        val clickIncrement: (cartItem: ShopItem)-> Unit = {viewModel.incrementItemCount(it)}
        val clickDecrement: (cartItem: ShopItem)-> Unit = {viewModel.decrementItemCount(it)}
        val clickAdditional: (cartItem: ShopItem)-> Unit = {viewModel.addAdditionalServices(it)}
        adapter = CartAdapter(clickIncrement = clickIncrement, clickDecrement = clickDecrement,
            clickAdditional = clickAdditional)
        binding.cartRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRcView.adapter = adapter

        viewModel.cart.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


    }


}