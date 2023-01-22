package com.onitsura12.farmdel.fragments.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.onitsura12.domain.models.Address
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentConfirmOrderBinding
import com.onitsura12.farmdel.recyclerView.AddressAdapter
import com.onitsura12.farmdel.recyclerView.OrderItemAdapter

class ConfirmOrderFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmOrderFragment()
    }


    private lateinit var viewModel: ConfirmOrderViewModel
    private lateinit var binding: FragmentConfirmOrderBinding
    private lateinit var addressAdapter: AddressAdapter
    private val cartAdapter: OrderItemAdapter = OrderItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmOrderBinding.inflate(layoutInflater)
        viewModel = ConfirmOrderViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        initAddressRcView()
        initCartRcView()

    }


    private fun initAddressRcView() {
        val markAddress: (address: Address)-> Unit = {viewModel.markAddress(address = it)}
        addressAdapter = AddressAdapter(markAddress = markAddress)
        binding.addressRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRcView.adapter = addressAdapter
        viewModel.addressList.observe(viewLifecycleOwner) {
            addressAdapter.submitList(it)
        }
    }

    private fun initCartRcView(){

        binding.cartRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRcView.adapter = cartAdapter
        viewModel.orderItemsList.observe(viewLifecycleOwner){
            cartAdapter.submitList(it)
        }
    }

    private fun initButtons(){
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
        binding.confirmButton.setOnClickListener {
            viewModel.createOrder()
            viewModel.cleanCart()
            findNavController().navigate(R.id.action_confirmOrderFragment_to_cartSuccessFragment)
        }
    }
}